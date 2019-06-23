module Grid ( Grid, fromWalls, addWall, isWall, isFree, maxBounds, allWalls ) where

import Data.Set

import Types

data Grid = Grid { maxBounds :: (Int, Int), walls :: Set Point }

fromWalls :: [Point] -> Grid
fromWalls ps = Grid (maxX,maxY) (fromList ps)
  where
    maxX = maximum [x | Point x _ <- ps]
    maxY = maximum [y | Point _ y <- ps]

isWall :: Grid -> Point -> Bool
isWall grid point = member point (walls grid)

isFree :: Grid -> Point -> Bool
isFree g = not . isWall g

allWalls :: Grid -> [Point]
allWalls grid = toList (walls grid)

instance Show Grid where
  showsPrec _ grd = let (maxX,maxY) = maxBounds grd
                        showSquare x y | isWall grd (Point x y) = '#'
                                       | otherwise = '.'
                        showRow y = [showSquare x y | x <- [0 .. maxX]]
                        showGrid h | h < 0 = id
                                   | otherwise = showString (showRow h) . showChar '\n'
                                                 . showGrid (h - 1)
                    in showGrid maxY

addWall :: Grid -> Point -> Grid
addWall grid point = grid{walls = insert point (walls grid)}

