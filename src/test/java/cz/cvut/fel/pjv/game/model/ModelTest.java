package cz.cvut.fel.pjv.game.model;

import cz.cvut.fel.pjv.game.Controller;
import cz.cvut.fel.pjv.game.GameData;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ModelTest {
    private final GameData data;
    private final Controller controller;


    public ModelTest() {
        data = new GameData();
        controller = new Controller();
        data.setBoard(new int[][] {{6,6},{6,6}});
        data.setEnemies(new String[][] {{"1", "1", "100", "25", "1", "coin"}});
        data.setPlayer(new String[]{"1","2","150", "150", "30", "120", "0.05", "up"});
    }

    @Test
    public void checkHits_enemyProjectileShouldLowerPlayerHP() {
        // arrange
        Model testModel = new Model(data,controller);
        Projectile mockedProj = Mockito.mock(Projectile.class);
        Mockito.when(mockedProj.getPosX()).thenReturn(1.0);
        Mockito.when(mockedProj.getPosY()).thenReturn(2.0);
        testModel.getEnemies().get(0).getProjectiles().add(mockedProj);
        int expectedResult = testModel.getPlayer().getHp() - testModel.getEnemies().get(0).getAttackDamage();

        // act
        testModel.checkHits();
        int result = testModel.getPlayer().getHp();


        // assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void updatePlayer_correctPLayerPositionUpdateTest() {
        // arrange
        Model testModel = new Model(data,controller);
        double[] expectedResult = new double[2];
        expectedResult[0] = 1.05;
        expectedResult[1] = 2.0;

        // act
        testModel.movePlayer('d');
        testModel.updatePlayer();
        double[] result = new double[2];
        result[0] = testModel.getPlayer().getPosX();
        result[1] = testModel.getPlayer().getPosY();

        // assert
        assertArrayEquals(expectedResult, result);
    }

    @Test
    public void itemPickUp_playerPicksUpItemOnTheGroundTest() {
        // arrange
        Model testModel = new Model(data, controller);
        Item item = new Item(1, 2);
        testModel.getItems().add(item);

        // act
        testModel.checkItemPickUp();
        Item result = testModel.getPlayer().getInventory().get(0);

        // assert
        assertEquals(item,result);
    }

    @Test
    public void openChest_playerOpensChestTest() {
        // arrange
        data.setBoard(new int[][] {{6, 6}, {6,5}});
        Model testModel = new Model(data, controller);
        Key mockedKey = Mockito.mock(Key.class);
        testModel.getPlayer().getInventory().add(mockedKey);

        // act
        testModel.movePlayer('d');
        testModel.updatePlayer();

        // assert
        assertEquals(1, testModel.getItems().size());
    }

    @Test
    public void killEnemy_EnemyIsSetDeadTest() {
        // arrange
        data.setEnemies(new String[][] {{"1", "1", "1", "25", "1", "coin"}}); // set enemy HP to 1, so it is killed with 1 shot
        Model testModel = new Model(data,controller);
        Enemy enemy = testModel.getEnemies().get(0);
        Projectile mockedProj = Mockito.mock(Projectile.class);
        Mockito.when(mockedProj.getPosX()).thenReturn(1.0);
        Mockito.when(mockedProj.getPosY()).thenReturn(1.0);
        testModel.getPlayer().getProjectiles().add(mockedProj);

        // act
        testModel.checkHits();

        // assert
        assertTrue(enemy.isDead());
    }

    @Test
    public void process1() { // movement test
        // arrange
        Model testModel = new Model(data, controller);

        // act 1 - face player towards given direction
        testModel.movePlayer('d');

        // assert 1 - face player towards given direction
        assertEquals("right", testModel.getPlayer().getFacing());
        assertArrayEquals(new double[] {0.05,0},testModel.getPlayer().getMovementSpeed());

        // act 2 - move
        testModel.updatePlayer();

        // assert 2 - move
        assertEquals(1.05, testModel.getPlayer().getPosX());
        assertEquals(2.0, testModel.getPlayer().getPosY());

        // act 3 - face player and try to move behind end of the board (should not move the player)
        testModel.movePlayer('s');
        testModel.updatePlayer();

        // assert 3 - face player and try to move behind end of the board (should not move the player)
        assertEquals(1.05, testModel.getPlayer().getPosX());
        assertEquals(2.0, testModel.getPlayer().getPosY());
    }

    @Test
    public void process2() { // shooting test
        // arrange
        Model testModel = new Model(data, controller);

        // act 1 - shoot
        testModel.getPlayer().shoot();

        // assert 1 - shoot
        assertEquals(1, testModel.getPlayer().getProjectiles().size());
    }

    @Test
    public void process3() { // hitting and killing and dropping items

    }

    @Test
    public void process4() { // pick key, open chest, pick coin
        // arrange
        data.setBoard(new int[][] {{6,6}, {6, 5}});
        Model testModel = new Model(data, controller);
        testModel.getItems().add(new Key(1, 2));

        // act 1 - pick up a key
        testModel.checkItemPickUp();

        // assert 1 - pick up a key
        assertTrue(testModel.getPlayer().hasKey());

        // act 2 - face towards a chest
        testModel.movePlayer('d');

        // assert 2 - face towards a chest
        assertEquals("right", testModel.getPlayer().getFacing());

        // act 3 - open a chest
        testModel.updatePlayer();

        // assert 3 - open a chest
        // opening chest means dropping item on the ground so there is exactly one item in items arraylist
        assertEquals(1, testModel.getItems().size());

        // act 3 - pickup a coin
        for (int i = 0; i<25; i++) {        // give player enough cycles to move
            testModel.updatePlayer();
        }
        testModel.checkItemPickUp();

        // assert 3 - pickup a coin
        assertEquals(0, testModel.getItems().size());
    }
}
