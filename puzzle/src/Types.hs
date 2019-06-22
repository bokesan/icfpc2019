module Types ( Point(..)
             , translate
             , north, east, south, west
             , neighbors
             , manhattanDistance
             , nat
             , point
             , Action(..))
 where

import Text.Parsec


data Point = Point !Int !Int deriving (Eq, Ord)

instance Show Point where
  showsPrec _ (Point x y) = showChar '(' . shows x . showChar ',' . shows y . showChar ')'

translate :: Int -> Int -> Point -> Point
translate dx dy (Point x y) = Point (x + dx) (y + dy)

north, east, south, west :: Point -> Point
north = translate 0 1
east  = translate 1 0
south = translate 0 (-1)
west  = translate (-1) 0

neighbors :: Point -> [Point]
neighbors p = [north p, east p, south p, west p]

manhattanDistance :: Point -> Point -> Int
manhattanDistance (Point x1 y1) (Point x2 y2) = abs (x1 - x2) + abs (y1 - y2)


nat :: Parsec String st Int
nat = read <$> many1 digit

point :: Parsec String st Point
point = do char '('
           x <- nat
           char ','
           y <- nat
           char ')'
           return (Point x y)


data Action = MoveUp | MoveDown | MoveLeft | MoveRight
            -- | Nop
            | TurnCW | TurnCCW
            | NewManipulator !Int !Int
            | Fast | Drill
            | PlaceBeacon
            | Teleport !Point
            deriving (Eq, Ord, Show)

encodeAction :: Action -> String
encodeAction MoveUp    = "W"
encodeAction MoveDown  = "S"
encodeAction MoveLeft  = "A"
encodeAction MoveRight = "D"
encodeAction TurnCW    = "E"
encodeAction TurnCCW   = "Q"
encodeAction (NewManipulator dx dy) = "B(" ++ show dx ++ "," ++ show dy ++ ")"
encodeAction Fast      = "F"
encodeAction Drill     = "L"
encodeAction PlaceBeacon = "R"
encodeAction (Teleport p) = "T" ++ show p

