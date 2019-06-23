import System.Exit

import Types
import Puzzle
import PuzzleSolver
import Task



main :: IO ()
main = do -- check samplePuzzle 5 "simple 1"
          check samplePuzzle2 60 "simple 2"

check :: Puzzle -> Int -> String -> IO ()
check puzzle expectedLength name =
    case solvePuzzle puzzle of
      Nothing -> do putStrLn (name ++ " failed!")
                    exitWith (ExitFailure 1)
      Just (s,g) -> if length (taskMap s) == expectedLength then
                       putStrLn (name ++ " successful")
                    else
                       do putStrLn (name ++ " wrong length " ++ show (length (taskMap s)))
                          exitWith (ExitFailure 1)


samplePuzzle = Puzzle { bNum = 1, eNum = 2,
                        tSize = 8,
                        vMin = 2, vMax = 20,
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
