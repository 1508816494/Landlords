package com.mym.landlords.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mym.landlords.ai.Game.Status;
import com.mym.landlords.card.Card;

/**
 * 代表玩家的实体类。
 * 
 * @author Muyangmin
 * @create 2015-3-18
 */
public final class Player {
	private String playerName;			//玩家名称
	private ArrayList<Card> handCards; // 手牌列表
	private boolean isLandlord; // 是否是地主
	private boolean isAiPlayer; // 是否是AI玩家
	private Player priorPlayer; // 上手玩家
	private Player nextPlayer; // 下手玩家
	private ArrayList<Card> lastCards; // 出的最后一手牌，用于AI判断和逻辑控制
	private int calledScore = Integer.MIN_VALUE;// 叫的分数, Integer.MIN_VALUE表示未赋值
	
	private AI aiRobot;				//机器AI

	/**
	 * 创建一个新的AI玩家实例。
	 * @deprecated 推荐使用 {@link #newAiPlayer(String)}代替。
	 */
	public static final Player newAiPlayer() {
		return newAiPlayer("");
	}
	/**
	 * 创建一个新的AI玩家实例。
	 * @param name 玩家名称。
	 */
	public static final Player newAiPlayer(String name) {
		Player player = new Player(true, name == null ? "" : name);
		player.aiRobot = new AI();
		return player;
	}

	/**
	 * 创建一个非AI玩家实例。
	 * @deprecated 推荐使用 {@link #newHumanPlayer(String)}代替。
	 */
	public static final Player newHumanPlayer() {
		return newHumanPlayer("");
	}

	/**
	 * 创建一个非AI玩家实例。
	 * @param name 玩家名称
	 */
	public static final Player newHumanPlayer(String name) {
		return new Player(false, name);
	}

	private Player(boolean isAi, String name) {
		isAiPlayer = isAi;
		playerName = name;
	}

	public int getCalledScore() {
		return calledScore;
	}

	public ArrayList<Card> getHandCards() {
		return handCards;
	}

	public ArrayList<Card> getLastCards() {
		return lastCards;
	}

	public Player getNextPlayer() {
		return nextPlayer;
	}

	public Player getPriorPlayer() {
		return priorPlayer;
	}

	/**
	 * 打出卡牌。
	 */
	protected final void giveCard(List<Card> toRemove) {

	}

	public boolean isAiPlayer() {
		return isAiPlayer;
	}

	public boolean isLandlord() {
		return isLandlord;
	}
	
	private final void checkAiPlayer(){
		if (!isAiPlayer){
			throw new RuntimeException("This method should not be invoked from a non-ai player.");
		}
	}
	
	/**
	 * 执行叫地主操作。最终得出的分数要使用 {@link #getCalledScore()}方法获得。
	 * @param minScore 地主最低分数（通常是本局前两位喊出来的）。
	 */
	public synchronized final void callLandlord(int minScore){
		checkAiPlayer();
		calledScore = aiRobot.callLandlord(handCards, minScore >= 0 ? minScore : 0);
	}

	/**
	 * 该方法使玩家进行下一个回合。可能使用到的输入有当前的游戏状态、上一个玩家的操作等。
	 * 
	 * @param currentGame
	 *            当前正在进行的游戏实例。如果为null或处于游戏结束后的某个阶段，则会抛出异常。
	 */
	public synchronized void nextRound(Game currentGame) {
		if (currentGame == null || currentGame.status == Status.ShowingAICards
				|| currentGame.status == Status.Gameover) {
			throw new IllegalArgumentException("invalid game instance.");
		}
//		if (currentGame.status == Status.CallingLandlord) {
//			if (calledScore != Integer.MIN_VALUE) {// 已经计算过，不再计算
//				return;
//			}
//			int last = priorPlayer.getCalledScore();
//			// 如果是第一家，则只需要大于0；否则需要大于上家
//			calledScore = AI.callLandlord(handCards, last >= 0 ? last : 0);
//			return;
//		}
	}
	

	/**
	 * 设置手牌并自动排序。
	 * @param handCards 手牌列表，不能为null。
	 */
	public void setHandCards(List<Card> handCards) {
		if (handCards == null) {
			throw new RuntimeException("handCards cannot be null.");
		}
		this.handCards = new ArrayList<>(handCards.size());
		this.handCards.addAll(handCards);
		Collections.sort(this.handCards);
	}

	/**
	 * 设置手牌并自动排序。
	 * @param cards 卡牌列表，不能为null。
	 */
	public void setHandCards(ArrayList<Card> cards) {
		if (cards == null) {
			throw new RuntimeException("handCards cannot be null.");
		}
		handCards = cards;
		Collections.sort(handCards);
	}

	/**
	 * 设置为地主并将底牌加入手中。
	 * 
	 * @param awardCards
	 *            底牌列表，不能为null。
	 */
	public void setLandlord(List<Card> awardCards) {
		if (awardCards == null) {
			throw new RuntimeException("awardCards cannot be null.");
		}
		this.isLandlord = true;
		this.handCards.addAll(awardCards);
		Collections.sort(this.handCards);
	}

	/**
	 * 设置座位。
	 * 
	 * @param prior
	 *            上家（左方）
	 * @param next
	 *            下家（右方）
	 */
	public void setSeat(Player prior, Player next) {
		this.priorPlayer = prior;
		this.nextPlayer = next;
	}

	public void setCalledScore(int calledScore) {
		this.calledScore = calledScore;
	}
	public String getPlayerName() {
		return playerName;
	}
}
