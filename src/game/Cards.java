package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.w3c.dom.html.HTMLImageElement;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/* Name: Roman Campbell
 * Project: 
 */

// This class will create all the different cards from a traditional 52 card stack
public class Cards 
{
	private char suit;
	private char rank;
	private FileInputStream cardImgPath;
	private ImageView cardView;
	private ImageView blankCardView;
	private String card_path;
	private String deck_path;
	private Image cardImg;
	private final static Integer ACE_CARD	= 11;
	private final static Integer TWO_CARD 	= 2;
	private final static Integer THREE_CARD = 3;
	private final static Integer FOUR_CARD 	= 4;
	private final static Integer FIVE_CARD 	= 5;
	private final static Integer SIX_CARD 	= 6;
	private final static Integer SEVEN_CARD = 7;
	private final static Integer EIGHT_CARD = 8;
	private final static Integer NINE_CARD 	= 9;
	private final static Integer TEN_CARD	= 10;
	private final static Integer JACK_CARD	= 10;
	private final static Integer KING_CARD	= 10;
	private final static Integer QUEEN_CARD	= 10;
	private static String absPath;
	private char blankChar;
	private String blankCardPath;
	private Image blankCardImg;
	private FileInputStream blankImgPath;
	private Integer playerWin 	= 0;
	private Integer playerLost 	= 0;
	private Integer dealerWin 	= 0;
	private Integer dealerLost	= 0;
	private char cardRank;
	private String cardRankName;
	private Integer cardScore;
	
	public Cards(char rank, char suit) throws FileNotFoundException
	{
		this.suit = suit;
		this.rank = rank;
		
		File file = new File("cards");
		absPath = file.getAbsolutePath();
		this.card_path = absPath+"/"+rank+suit+".gif";
		this.deck_path = absPath;
		
		this.cardImgPath = new FileInputStream(card_path);
		this.cardImg = new Image(cardImgPath);
	}
	
	// Blank card constructor
	public Cards() throws FileNotFoundException
	{
		
		File file = new File("blank_card");
		absPath = file.getAbsolutePath();
		blankCardPath = absPath+"/b.gif";
		
		blankImgPath = new FileInputStream(blankCardPath);
		blankCardImg = new Image(blankImgPath);
	}
	
	// Get card rank
	public String getCardRank()
	{
		switch(cardRank)
        {
        	case 'j':
        		cardRankName = "jack";
        		break;
        	case 'k':
        		cardRankName = "king";
        	case 'q':
        		cardRankName = "queen";
        		break;
        	case '1':
        		cardRankName = "ten";
        		break;
        	case 'a':
        		cardRankName = "ace";
        		break;
        	default:
        		cardRankName = "single digit";
        }
		
		return cardRankName;
	}
	
	// Retrieve image of blank card
	public Image getBlankCardImg()
	{
		return this.blankCardImg;
	}
	
	// Retrieve image of card
	public Image getCardImg()
	{
		return this.cardImg;
	}
	
	// Display regular cards
	public ImageView displayCardView()
	{
		this.cardView = new ImageView(this.getCardImg());
		return this.cardView;
	}
	
	// Display blank card
	public ImageView displayBlankCardView()
	{
		this.blankCardView = new ImageView(this.getBlankCardImg());
		return blankCardView;
	}
	
	// Get image path of card
	public String getImgPath()
	{
		return this.card_path;
	}
	
	// Get entire deck file path
	public String getDeckPath()
	{
		return this.deck_path;
	}
	
	// Get value of card
	public Integer getCardValue()
	{
		cardRank = this.rank;
		switch(cardRank)
        {
			case 'a':
				cardScore = ACE_CARD;
				break;
        	case 'j':
        		cardScore = JACK_CARD;
        		break;
        	case 'k':
        		cardScore = KING_CARD;
        	case 'q':
        		cardScore = QUEEN_CARD;
        		break;
        	case '2':
        		cardScore = TWO_CARD;
        		break;
        	case '3':
        		cardScore = THREE_CARD;
        		break;
        	case '4':
        		cardScore = FOUR_CARD;
        		break;
        	case '5':
        		cardScore = FIVE_CARD;
        		break;
        	case '6':
        		cardScore = SIX_CARD;
        		break;
        	case '7':
        		cardScore = SEVEN_CARD;
        		break;
        	case '8':
        		cardScore = EIGHT_CARD;
        		break;
        	case '9':
        		cardScore = NINE_CARD;
        		break;
        	case 't':
        		cardScore = TEN_CARD;
        }
		return cardScore;
		
	}
	
	
	
	// SET PLAYER WON
	public Integer setPlayerWon()
	{
		return playerWin++;
	}
	
	// GET PLAYER WINS
	public Integer getPlayerWins()
	{
		return playerWin;
	}
	
	// SET PLAYER LOST
	public Integer setPlayerLost()
	{
		return playerLost++;
	}
	
	// GET PLAYER LOST
	public Integer getPlayerLost()
	{
		return playerLost;
	}
	
	// SET DEALER WON
	public Integer setDealerWon()
	{
		return dealerWin++;
	}
	
	// GET DEALER WON
	public Integer getDealerWins()
	{
		return dealerWin;
	}
	
	// SET DEALER LOST
	public Integer setDealerLost()
	{
		return dealerLost++;
	}
	
	// GET DEALER LOST
	public Integer getDealerLost()
	{
		return dealerLost;
	}
}