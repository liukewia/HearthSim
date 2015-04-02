package com.hearthsim.test.groovy.card.spell

import com.hearthsim.card.minion.concrete.KoboldGeomancer
import com.hearthsim.card.minion.concrete.Voidwalker
import com.hearthsim.card.spellcard.concrete.Demonheart
import com.hearthsim.card.spellcard.concrete.Flamecannon
import com.hearthsim.model.BoardModel;
import com.hearthsim.player.playercontroller.BruteForceSearchAI;
import com.hearthsim.test.groovy.card.CardSpec;
import com.hearthsim.test.helpers.BoardModelBuilder
import com.hearthsim.util.tree.HearthTreeNode;
import com.hearthsim.util.tree.RandomEffectNode
import com.hearthsim.card.spellcard.concrete.Soulfire
import com.hearthsim.card.spellcard.concrete.Polymorph
import com.hearthsim.card.minion.concrete.WarGolem

import static com.hearthsim.model.PlayerSide.CURRENT_PLAYER
import static com.hearthsim.model.PlayerSide.WAITING_PLAYER
import static org.junit.Assert.*

class FlamecannonSpec extends CardSpec {

    HearthTreeNode root
    BoardModel startingBoard

    def setup() {
        startingBoard = new BoardModelBuilder().make {
            currentPlayer {
                hand([Flamecannon])
                field([[minion: Voidwalker], [minion: WarGolem]])
                mana(10)
            }
            waitingPlayer {
                field([[minion: Voidwalker],[minion: WarGolem]])
            }
        }

        root = new HearthTreeNode(startingBoard)
    }

    def "returned node is RNG"() {
        def copiedBoard = startingBoard.deepCopy()
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 0, root)

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 2

        assertBoardDelta(copiedBoard, ret.data_) {
            currentPlayer {
                mana(8)
                numCardsUsed(1)
            }
        }
    }

    def "hits enemy minions"() {
        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 0, root)
        def copiedBoard = ret.data_.deepCopy()

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 2

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                removeCardFromHand(Flamecannon)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                removeCardFromHand(Flamecannon)
            }
            waitingPlayer {
                updateMinion(1, [deltaHealth: -4])
            }
        }
    }

    def "is effected by spellpower"() {
        startingBoard.placeMinion(CURRENT_PLAYER, new KoboldGeomancer())

        def theCard = root.data_.getCurrentPlayer().getHand().get(0)
        def ret = theCard.useOn(WAITING_PLAYER, 0, root)
        def copiedBoard = ret.data_.deepCopy()

        expect:
        ret != null
        ret instanceof RandomEffectNode
        ret.numChildren() == 2

        HearthTreeNode child0 = ret.getChildren().get(0);
        assertBoardDelta(copiedBoard, child0.data_) {
            currentPlayer {
                removeCardFromHand(Flamecannon)
            }
            waitingPlayer {
                removeMinion(0)
            }
        }

        HearthTreeNode child1 = ret.getChildren().get(1);
        assertBoardDelta(copiedBoard, child1.data_) {
            currentPlayer {
                removeCardFromHand(Flamecannon)
            }
            waitingPlayer {
                updateMinion(1, [deltaHealth: -5])
            }
        }
    }
}
