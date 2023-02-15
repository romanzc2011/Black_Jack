package game;

import java.util.List;
import java.util.Optional;

import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;

public class ScoreChecker 
{
	private boolean playerWon = false;
	private boolean playerLost = false;
	private boolean dealerWon = false;
	private boolean playAgain = false;
	private Integer playerWins = 0;
	private Double betWinnings = 0.0;
	private Double betAmount = 0.0;
	private Integer playerLoses = 0;
	private Integer dealerWins = 0;
	private Integer dealerLoses = 0;
	private TextInputDialog prompt;
	
	public ScoreChecker()
	{
		
	}
	
    public boolean checkScore(Cards card, int dealerScore, int playerScore, List<Cards> cardDealerList, DeckCards cardDeck, HBox dealerHand, boolean playerStood, double betAmount, double betWinnings) 
    {
        if(dealerScore > 21 && playerScore <= 21) 
        {
            System.out.println("Player won");
            card.setDealerLost();
            card.setPlayerWon();
            
            
            this.setAddPlayerWins();
            this.setAddDealerLoses();
            
            this.setBetPlayerWinnings(betWinnings, betAmount);
            
            return this.setPlayerWon();
        }
        
        if(dealerScore >= 17)
        {
        	System.out.println("Dealer must stand");
        	if(playerScore > dealerScore)
        	{
        		System.out.println("Player won");
        		playerWon = true;
        		this.setBetPlayerWinnings(betWinnings, betAmount);
        		return playerWon;
        		
        	} else {
        		System.out.println("Dealer won");
        		playerLoses++;
        		dealerWon = true;
        		this.setBetPlayerLoses(betWinnings, betAmount);
        		return dealerWon;
        	}
        }
        
        if(playerScore == 21 && dealerScore <= 21) 
        {
            
            this.setAddPlayerWins();
            this.setAddDealerLoses();
            
            System.out.println("Player won");
            this.setBetPlayerWinnings(betWinnings, betAmount);
            return this.setPlayerWon();
        }
        
        if(playerScore > 21 && dealerScore <= 21) {
            card.setPlayerLost();
            card.setDealerWon();
            
            System.out.println("Player lost");
            this.setAddPlayerLoses();
            this.setAddDealerWins();
            
            this.setBetPlayerLoses(betWinnings, betAmount);
            return this.setPlayerLost();
        }
        
        return false;
    }
    
    public boolean playAgainPrompt()
	{
    	prompt = new TextInputDialog();
    	prompt.setTitle("Black Jack");
    	prompt.setHeaderText("Play Again?");
    	
    	Optional<String> result = prompt.showAndWait();
    	
    	if(result.isPresent() && result.get().toLowerCase().equals("y"))
    	{
    		playAgain = true;
    		return playAgain;
    	} else {
    		return false;
    	}
    	
	}
    
    // #################################################################################
    // #################################################################################
    // SETTERS
    // #################################################################################
    // #################################################################################
    
    public boolean setPlayerLost()
    {
    	this.playerWon = false;
    	this.playerLost = true;
    	return this.dealerWon = true;
    }
    
    public void setAddDealerWins()
    {
    	this.dealerWins++;
    }
    
    public void setAddPlayerWins()
    {
    	this.playerWins++;
    }
    
    public boolean setPlayerWon()
    {
    	this.dealerWon = false;
    	return playerWon = true;
    }
    
    public void setAddPlayerLoses()
    {
    	this.playerLoses++;
    }
    
    public void setAddDealerLoses()
    {
    	this.dealerLoses++;
    }
    
    public void setBetPlayerWinnings(double betWinnings, double betAmount)
    {
    	this.betWinnings += betAmount;
    }
    
    public void setBetPlayerLoses(double betWinnings, double betAmount)
    {
    	this.betWinnings -= betAmount;
    }
    
    
    // #################################################################################
    // #################################################################################
    // GETTERS
    // #################################################################################
    // #################################################################################
    
    
    public boolean getDidPlayerWin()
    {
    	return this.playerWon;
    }
    
    public boolean getDealerWon()
    {
    	return this.dealerWon;
    }
    
    public boolean getDidPlayerLose()
    {
    	return this.playerLost;
    }
    
    public Integer getPlayerWins()
    {
    	return this.playerWins;
    }
    
    public Integer getPlayerLoses()
    {
    	return this.playerLoses;
    }
   
    public Integer getDealerWins()
    {
    	return this.dealerWins;
    }
    
    public Integer getDealerLoses()
    {
    	return this.dealerLoses;
    }
    
    public Double getPlayerWinnings()
    {
    	return this.betWinnings;
    }
}
