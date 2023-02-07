
package comp3350.acci.tests.business;

//java imports
import junit.framework.TestCase;
import org.junit.Test;

//project imports
import comp3350.acci.business.RecipeManager;
import comp3350.acci.objects.Recipe;
import comp3350.acci.objects.User;

public class RecipeInsertionTest extends TestCase {

    public RecipeInsertionTest(String arg0 ) {
        super(arg0);
    }

    @Test
    public void testCreateRecipe() {
        System.out.println("\nStarting recipe creation test");
        RecipeManager manager = new RecipeManager();
        User user = new User("Caelan", "I'm the coolest");
        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");
        assertNotNull(rep1);
        assertEquals(user, rep1.getAuthor());
        assertEquals("PB&J", rep1.getName());
        assertEquals("Put peanut butter and jam on toast.", rep1.getInstructions());
        assertFalse(rep1.getIsPrivate());
        System.out.println("Created Recipe had all expected fields");
    }
    @Test
    public void testRecipePrivacy() {
        System.out.println("\nStarting Recipe Privacy Test:");

        User user = new User("Caelan", "I'm the coolest");
        RecipeManager manager = new RecipeManager();

        Recipe rep1 = manager.createRecipe(user, "PB&J", "Put peanut butter and jam on toast.", false, "Hard");

        assertFalse(rep1.getIsPrivate());

        manager.changePrivacy(rep1);

        assertTrue(rep1.getIsPrivate());

        System.out.println("changePrivacy test successfully");
    }
}
