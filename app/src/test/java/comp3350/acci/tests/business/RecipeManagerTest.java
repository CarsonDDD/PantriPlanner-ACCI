
package comp3350.acci.tests.business;

//java imports
import junit.framework.TestCase;
import org.junit.Test;

//project imports
import comp3350.acci.business.RecipeManager;
import comp3350.acci.objects.Recipe;
import comp3350.acci.objects.User;

public class RecipeManagerTest extends TestCase {

    public RecipeManagerTest(String arg0 ) {
        super(arg0);
    }

    @Test
    public void testCreateRecipe() {
        System.out.println("\nStarting recipe creation test");
        RecipeManager manager = new RecipeManager();
        User user = new User("Caelan", "I'm the coolest");
        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        Recipe manRep = manager.getRecipeFromPersistence(rep1);

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
        RecipeManager manager = new RecipeManager();

        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        assertFalse(rep1.getIsPrivate());

        manager.changePrivacy(rep1);

        assertTrue(rep1.getIsPrivate());

        System.out.println("changePrivacy tested successfully");
    }
    @Test
    public void testRecipeInstructions() {
        System.out.println("\nStarting recipe instruction Tests:");

        User user = new User("Caelan", "I'm the coolest");
        RecipeManager manager = new RecipeManager();

        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        assertNotNull(rep1.getInstructions());
        assertNotNull(manager.getInstructions(rep1));

        assertEquals(rep1.getInstructions(), manager.getInstructions(rep1));

        String newInstructions = "Put the jam and peanut butter on some bread";

        manager.setInstructions(rep1, newInstructions);

        assertEquals(newInstructions, manager.getInstructions(rep1));
        assertEquals(newInstructions, manager.getInstructionsByID(rep1.getRecipeID()));

        System.out.println("Recipe Instructions tested Successfully");
    }
}
