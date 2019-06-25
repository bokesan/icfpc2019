{-# LANGUAGE BangPatterns #-}
module PuzzleSolver (solvePuzzle) where

import Data.List (sort)
import System.Random

import Types
import Puzzle
import Task
import Grid

solvePuzzle :: Puzzle -> Maybe (Task, Grid)
solvePuzzle puzzle = fmap (fillTask puzzle) $ go start (oSqs puzzle)
  where
    start = border (tSize puzzle)
    go grid []     = Just grid
    go grid (o:os) = case grow puzzle grid (nearestTo grid o) o of
                       Nothing -> Nothing
                       Just g -> go g os
                       
fillTask :: Puzzle -> Grid -> (Task, Grid)
fillTask puzzle grid = let map' = wallsToPoly grid'
                           grid' = complicate puzzle grid
                           (start:open) = allOpen grid'
                           !maxBoosters = maximum [mNum puzzle, fNum puzzle, dNum puzzle,
                                                  rNum puzzle, cNum puzzle, xNum puzzle]
                           !step = (length open - 50) `quot` maxBoosters
                           manip = take (mNum puzzle) (allNth step (drop 40 open))
                           fast  = take (fNum puzzle) (allNth step (drop 41 open))
                           drill = take (dNum puzzle) (allNth step (drop 42 open))
                           tele  = take (rNum puzzle) (allNth step (drop 43 open))
                           clone = take (cNum puzzle) (allNth step (drop 44 open))
                           spawn = take (xNum puzzle) (allNth step (drop 45 open))
                       in (Task map' start [] (map (BoosterLocation B) manip ++
                                               map (BoosterLocation F) fast ++
                                               map (BoosterLocation L) drill ++
                                               map (BoosterLocation X) spawn ++
                                               map (BoosterLocation C) clone ++
                                               map (BoosterLocation R) tele),
                            grid')

allNth :: Int -> [a] -> [a]
allNth _ [] = []
allNth n (x:xs) = x : allNth n (drop (n-1) xs)

complicate :: Puzzle -> Grid -> Grid
complicate puzzle grid = go rng grid
  where
    !n = (vMin puzzle + vMax puzzle) `quot` 2
    (maxX,maxY) = maxBounds grid
    rng = mkStdGen 4711
    (lo,_) = genRange rng
    nextInt lim rng = let (n, rng') = next rng in ((n - lo) `rem` lim, rng')
    go rng g = if length (wallsToPoly g) >= n then g
               else let (x, rng')  = nextInt maxX rng
                        (y, rng'') = nextInt maxY rng'
                        p = Point x y
                    in
                        if isWall g p || p `elem` iSqs puzzle then
                           go rng'' g
                        else
                           case grow puzzle g (nearestTo g p) p of
                             Nothing -> go rng'' g
                             Just g' -> go rng'' g'
    

border :: Int -> Grid
border n = fromWalls ([Point 0  i | i <- [0 .. n']] ++
                      [Point n' i | i <- [0 .. n']] ++
                      [Point i  0 | i <- [0 .. n']] ++
                      [Point i n' | i <- [0 .. n']])
  where n' = n - 1

nearestTo :: Grid -> Point -> Point
nearestTo grid point = snd (minimum [(distanceSquared p point, p) | p <- allWalls grid])

-- grow puzzle grid src dest
--  grow walls from existing wall src to and including dest
grow :: Puzzle -> Grid -> Point -> Point -> Maybe Grid
grow puzzle grid src dest
  | src == dest = Just grid
  | otherwise   = let ans = sort [(manhattanDistance dest p, p) | p <- neighbors src, isFree grid p, p `notElem` iSqs puzzle]
                  in firstJust [grow puzzle (addWall grid p) p dest | (_,p) <- ans]

firstJust :: [Maybe a] -> Maybe a
firstJust (x@(Just _) : _) = x
firstJust (Nothing : xs) = firstJust xs
firstJust [] = Nothing

allOpen :: Grid -> [Point]
allOpen grid = [Point x y | x <- [0 .. maxX], y <- [0 .. maxY], isFree grid (Point x y)]
  where 
    (maxX, maxY) = maxBounds grid

leastOpen :: Grid -> Point
leastOpen grid = head (allOpen grid)

wallsToPoly :: Grid -> [Point]
wallsToPoly grid = compress least (least : follow least)
  where
    least = leastOpen grid
    follow p = let p' = next p in
               if p' == least then []
                 else p' : follow p'
    next p | isWall grid (translate (-1) (-1) p) && isFree grid (south p) = south p
           | isWall grid (south p) && isFree grid p = east p
           | isWall grid p && isFree grid (west p) = north p
           | otherwise = west p

compress :: Point -> [Point] -> [Point]
compress first (Point x1 y1 : Point x2 y2 : Point x3 y3 : ps)
  | (x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3)
                  = compress first $ Point x1 y1 : Point x3 y3 : ps
compress (Point x3 y3) [Point x1 y1, Point x2 y2]
  | (x1 == x2 && x2 == x3) || (y1 == y2 && y2 == y3) = [Point x1 y1]
compress first (p : ps) = p : compress first ps
compress _ [] = []
             
