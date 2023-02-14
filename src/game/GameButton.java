package game;

import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;

/* Name: Roman Campbell
 * Course: CSC 285
 * Project: 
 * File Name:
 */

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;

public class GameButton 
{
	private String buttonType;
	private int buttonHeight;
	private int buttonWidth;
	private String textSize;
	private String action;
	private Button button;
	private DeckCards deckCards;
	private String response;
	private TextInputDialog prompt;
	private boolean playAgain;
	
	public GameButton(String type, String textSize, int height, int width, String action, DeckCards deckCards)
	{
		this.buttonType 	= type;
		this.buttonHeight 	= height;
		this.buttonWidth 	= width;
		this.action 		= action;
		this.textSize		= textSize;
		this.deckCards		= deckCards;
		
		this.button = new Button(type);
		
		this.button.setPrefHeight(height);
		this.button.setPrefWidth(width);
		
		this.button.setStyle("-fx-background-radius: 25;"
				+ "-fx-font-weight: bold;"
				+ "-fx-font-family: Sans-serif;"
				+ "-fx-background-color: gold;"
				+ "-fx-font-size: " + textSize);
	}
	
	public Button getButton()
	{
		return button;
	}
}
