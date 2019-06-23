module Main where

import System.IO
import System.Environment (getArgs)
import Text.Parsec

import Types
import Puzzle
import PuzzleSolver
import Task

                        

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
