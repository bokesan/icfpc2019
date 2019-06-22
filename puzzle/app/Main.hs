module Main where

import System.IO
import System.Environment (getArgs)
import Text.Parsec

import Types
import Puzzle
import PuzzleSolver
import Task


samplePuzzle = Puzzle { bNum = 1, eNum = 2,
                        tSize = 8,
                        vMin = 0, vMax = 0,
                        mNum = 0, fNum = 0, dNum = 0,
                        rNum = 0, cNum = 0, xNum = 0,
                        iSqs = [Point 4 4],
                        oSqs = [Point 4 3] }

samplePuzzle2 = Puzzle { bNum = 1, eNum = 2,
                         tSize = 20,
                         vMin = 20, vMax = 100,
                         mNum = 0, fNum = 1, dNum = 0,
                         rNum = 0, cNum = 0, xNum = 0,
                         iSqs = [Point 4 4, Point 10 13],
                         oSqs = [Point 4 3, Point 8 13] }
                        

main :: IO ()
main =  do [specFile] <- getArgs
           handle <- openFile specFile ReadMode  
           spec <- hGetContents handle  
           case parse puzzle "" spec of
             Left err -> print err
             Right puzzle -> solve puzzle
           hClose handle


solve :: Puzzle -> IO ()
solve puzzle = -- do print puzzle
                  case solvePuzzle puzzle of
                    Nothing -> putStrLn "No solution found"
                    Just (t,g)  -> do -- putStrLn "Task:"
                                      putStrLn (show t)
                                      -- putStrLn ("The map has " ++ show (length (taskMap t)) ++ " vertices")
                                      -- putStrLn "Grid:"
                                      -- putStrLn (show g)
