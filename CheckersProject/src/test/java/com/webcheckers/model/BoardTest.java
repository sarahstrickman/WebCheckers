package com.webcheckers.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest
{
    public void test_board_object()
    {
        Board test = new Board();
        assertAll(
                () -> {assertNotNull(test, "the board was successfully created");},
                () -> {assertEquals(test.getAllSpaces().size(), 8, "the row size is correct");},
                () -> {assertEquals(test.getAllSpaces().get(0).size(), 8, "the column size is correct");}
        );
    }

    public void test_spaces_and_move()
    {
        Board test = new Board();
        assertAll(
                () -> {assertNotNull(test.getSpace(0, 0).getPiece(), "the piece exists");},
                () -> {assertNull(test.getSpace(7, 6).getPiece(), "there is no piece on the second last spot");}
        );
        assertAll(
                () -> {assertFalse(test.move(test.getSpace(0,0), test.getSpace(0, 1)), "can't move to adjacent space");},
                () -> {assertFalse(test.move(test.getSpace(0, 0), test.getSpace(1,1)), "can't move to place where piece exists");},
                () -> {assertTrue(test.move(test.getSpace(2, 2), test.getSpace(3, 3)), "move should be successful");}
        );
    }

    @Test
    public void test()
    {
        test_board_object();
        test_spaces_and_move();
    }

}
