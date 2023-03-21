
package comp3350.acci.tests.business;

//java imports
import static org.mockito.Mockito.when;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

//project imports
import comp3350.acci.business.implementation.RecipeCreator;
import comp3350.acci.business.interfaces.RecipeManager;
import comp3350.acci.objects.Recipe;
import comp3350.acci.objects.User;
import comp3350.acci.persistence.RecipePersistence;
import comp3350.acci.persistence.stubs.RecipePersistenceStub;

public class RecipeManagerTest extends TestCase {
    @Mock
    private RecipePersistence repMock;
    private RecipeManager manager;
    public RecipeManagerTest(String arg0 ) {
        super(arg0);

    }

    //TODO LIST
    //1. add @Before and @After for set up and teardown of tests
    //2. Turn stubs into mocks (mockito)

    @Before
    public void setUp() {
        repMock = Mockito.mock(RecipePersistence.class);
        manager = new RecipeCreator((repMock));
    }
    @After
    public void tearDown() {
        repMock = null;
        manager = null;
    }
    @Test
    public void testCreateRecipe() {
        System.out.println("\nStarting recipe creation test");
//        repMock = Mockito.mock(RecipePersistence.class);

//        Mockito.when(repMock.insertRecipe(Recipe rep)).thenReturn(Recipe rep);
//        RecipeManager manager = new RecipeCreator(repMock);
        User user = new User("Caelan", "I'm the coolest");
        Recipe testRep = new Recipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");


        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");
//        Mockito.verify(repMock).insertRecipe();
        Recipe manRep = manager.getRecipeFromPersistence(rep1);

        Recipe rep2 = manager.createRecipe(null, "", "", false, "");
        assertNull(rep2);
        assertNotNull("Recipe manager should not have given back a null recipe",rep1);
        assertNotNull("Recipe manager should successfully give back a non-null recipe from the persistence",manRep);

        assertEquals("The recipe given back should have the same author",user, manRep.getAuthor());

        assertEquals("recipe name should not have been changed by manager","PB&J", rep1.getName());
        assertEquals("recipe name should not have been changed by persistence","PB&J", manRep.getName());

        assertEquals("Instructions should not have been changed by manager","Put peanut butter and jam on toast.", rep1.getInstructions());
        assertEquals("Instructions should not have been changed by persistence","Put peanut butter and jam on toast.", manRep.getInstructions());

        assertFalse("Recipe privacy should not have been changed by manager",rep1.getIsPrivate());
        assertFalse("Recipe privacy should not have been changed by persistence",manRep.getIsPrivate());
        System.out.println("Created Recipe had all expected fields");
    }
    @Test
    public void testRecipePrivacy() {
        System.out.println("\nStarting Recipe privacy Test:");

        User user = new User("Caelan", "I'm the coolest");
        RecipeManager manager = new RecipeCreator(new RecipePersistenceStub());

        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        assertFalse("Recipe should be public (unchanged)",rep1.getIsPrivate());

        manager.changePrivacy(rep1);

        assertTrue("Recipe privacy should be changed to true",rep1.getIsPrivate());

        manager.changePrivacy(rep1);

        assertFalse("Recipe should be changed to false",rep1.getIsPrivate());

        System.out.println("changePrivacy tested successfully");
    }
    @Test
    public void testRecipeInstructions() {
        System.out.println("\nStarting recipe instruction Tests:");

        User user = new User("Caelan", "I'm the coolest");
        RecipeManager manager = new RecipeCreator(new RecipePersistenceStub());

        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        assertNotNull("Instructions field should be non-null",rep1.getInstructions());
        assertNotNull("Getter from manager should produce non-null",manager.getInstructions(rep1));

        assertEquals("recipe from persistence and from recipe should be equal",rep1.getInstructions(), manager.getInstructions(rep1));

        String newInstructions = "Put the jam and peanut butter on some bread";

        manager.setInstructions(rep1, newInstructions);

        assertEquals("Recipe should have new instructions in persistence",newInstructions, manager.getInstructions(rep1));
        assertEquals("Recipe instructions by id should be the new instructions",newInstructions, manager.getInstructionsByID(rep1.getRecipeID()));

        System.out.println("Recipe Instructions tested Successfully");
    }
    @Test
    public void testGetNullRecipe() {
        System.out.println("\nStarting null recipe search Tests:");

        RecipeManager manager = new RecipeCreator(new RecipePersistenceStub());

        User user = new User("Caelan", "I'm the coolest");
        Recipe rep1 = new Recipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");


        assertNull("Result of nonexistent recipe id should be NULL", manager.getRecipeByID(100));
        assertNull("Result of nonexistent recipe search should be NULL",manager.getRecipeFromPersistence(rep1));

        System.out.println("Null recipe search tested Successfully");
    }
}
