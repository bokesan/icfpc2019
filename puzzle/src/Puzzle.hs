module Puzzle where

import Text.Parsec

import Types

data Puzzle = Puzzle { bNum :: !Int -- block number
                     , eNum :: !Int -- epoch number
                     , tSize :: !Int
                     , vMin :: !Int
                     , vMax :: !Int
                     , mNum :: !Int
                     , fNum :: !Int
                     , dNum :: !Int
                     , rNum :: !Int
                     , cNum :: !Int
                     , xNum :: !Int
                     , iSqs :: [Point]
                     , oSqs :: [Point]
                     } deriving Show

puzzle :: Parsec String st Puzzle
puzzle = do bN <- nat
            char ','
            eN <- nat
            char ','
            tS <- nat
            char ','
            vMi <- nat
            char ','
            vMa <- nat
            char ','
            mN <- nat
            char ','
            fN <- nat
            char ','
            dN <- nat
            char ','
            rN <- nat
            char ','
            cN <- nat
            char ','
            xN <- nat
            char '#'
            iS <- point `sepBy` char ','
            char '#'
            oS <- point `sepBy` char ','
            return (Puzzle bN eN tS vMi vMa mN fN dN rN cN xN iS oS)
