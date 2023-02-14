package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/* Name: Roman Campbell
 * Project: Black_Jack
 */

public class DeckCards 
{
	private ArrayList<File> deckCards;
	private ArrayList<Cards> gameCards = new ArrayList<>();
	private Integer cardRankScore;
	
	private Cards card;
	private ImageView cardView;
	private File[] files;
	
	public DeckCards() throws FileNotFoundException
	{
		// Create a complete deck of cards by iterating through all card files in the cards directory
		// Use Cards object to create the cards as it iterates thru directory
		
		File fileDir = new File("cards");
		String absPath = fileDir.getAbsolutePath();
		
		this.files = fileDir.getAbsoluteFile().listFiles();
		this.deckCards = new ArrayList<File>(53);
		
		
		for(File file : files)
		{
			if(file.isFile())
			{
				deckCards.add(file);
			}
		}
	}
	
	// ###############################################################################
	// NEW GAME
	public ArrayList<File> newGame()
	{
		this.deckCards = new ArrayList<File>(53);
		
		for(File file : files)
		{
			if(file.isFile())
			{
				this.deckCards.add(file);
			}
		}
		this.shuffleDeck();
		return this.deckCards;
	}
	
	public ArrayList<Cards> getCards(int count) throws FileNotFoundException
	{
		Iterator<File> iterator = deckCards.iterator();
	    int i = 0;
	    while (iterator.hasNext() && i < count)
	    {
	        File file = iterator.next();
	        String fileName = file.getName();

	        char cardRank = fileName.charAt(0);
	        char cardSuit = fileName.charAt(1);

	        this.card = new Cards(cardRank, cardSuit);
	        this.gameCards.add(this.card);

	        iterator.remove();
	        i++;
	    }
	    
	    return this.gameCards;
	}

	// ###############################################################################
	// SIZE OF DECK
	public Integer getDeckSize()
	{
		return this.deckCards.size();
	}
	
	// ###############################################################################
	// REMOVE CARD FROM DECK
	public ArrayList<File> removeCard(int count)
	{
		for(int i = 0; i <= count; i++)
		{
			if(!this.deckCards.isEmpty())
			{
				this.deckCards.remove(0);
			}
		}
		return this.deckCards;
	}
	
	public void shuffleDeck()
	{
		// Need more randomness then shuffle, add seed
		Collections.shuffle(this.deckCards, new Random(System.currentTimeMillis()));
	}
	
	// ###############################################################################
	// CLEAR GAME CARDS
	public void clearGameCards()
	{
		this.gameCards.clear();
	}

	
	// ###############################################################################
	// DECK CARDS
	public void clearDeckCards()
	{
		this.deckCards.clear();
		this.gameCards.clear();
	}
	
}
