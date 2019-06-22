module Task ( Task(..)
            , BoosterCode(..)
            , BoosterLocation(..)
            ) where

import Types

data Task = Task { taskMap :: [Point]
                 , initial :: Point
                 , obstacles :: [Point]
                 , boosters :: [BoosterLocation]
                 }

instance Show Task where
  showsPrec _ task = showSep (showChar ',') (taskMap task)
                   . showChar '#'
                   . shows (initial task)
                   . showChar '#'
                   . showSep (showChar ';') (obstacles task)
                   . showChar '#'
                   . showSep (showChar ';') (boosters task)

showSep :: (Show a) => ShowS -> [a] -> ShowS
showSep _ [] = id
showSep _ [x] = shows x
showSep sep (x:xs) = shows x . sep . showSep sep xs

data BoosterCode = B | F | L | X | C | R
                 deriving (Eq, Ord, Show)

data BoosterLocation = BoosterLocation BoosterCode Point

instance Show BoosterLocation where
  showsPrec _ (BoosterLocation code p) = shows code . shows p

