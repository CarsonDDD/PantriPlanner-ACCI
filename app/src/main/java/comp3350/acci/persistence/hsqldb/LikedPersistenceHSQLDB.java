package comp3350.acci.persistence.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import comp3350.acci.application.Services;
import comp3350.acci.objects.Ingredient;
import comp3350.acci.objects.Liked;
import comp3350.acci.objects.Pantry;
import comp3350.acci.objects.Recipe;
import comp3350.acci.objects.User;
import comp3350.acci.persistence.LikedPersistence;
import comp3350.acci.persistence.RecipePersistence;
import comp3350.acci.persistence.UserPersistence;

public class LikedPersistenceHSQLDB implements LikedPersistence {

    private final String dbPath;

    public LikedPersistenceHSQLDB(final String dbPath) {
        this.dbPath = dbPath;
    }

    private Connection connection() throws SQLException {
        return DriverManager.getConnection("jdbc:hsqldb:file:" + dbPath + ";shutdown=true", "SA", "");
    }

    private Liked fromResultSet(final ResultSet rs) throws SQLException {
        final int recipeID = rs.getInt("recipeID");
        final int userID = rs.getInt("userID");
        UserPersistence userPersistence = Services.getUserPersistence();
        RecipePersistence recipePersistence = Services.getRecipePersistence();
        User user = userPersistence.getUser(userID);
        Recipe recipe = recipePersistence.getRecipeByID(recipeID);

        return new Liked(user, recipe);
    }

    @Override
    public List<Liked> getLikeds() {
        final List<Liked> likeds = new ArrayList<>();

        try (final Connection c = connection()) {
            final Statement st = c.createStatement();
            final ResultSet rs = st.executeQuery("SELECT * FROM liked");
            while (rs.next())
            {
                final Liked liked = fromResultSet(rs);
                likeds.add(liked);
            }
            rs.close();
            st.close();

            return likeds;
        }
        catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }

    @Override
    public Liked insertLiked(Liked liked) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("INSERT INTO liked (userID, recipeID) VALUES(?, ?)");
            st.setInt(1, liked.getUser().getUserID());
            st.setInt(2, liked.getRecipe().getRecipeID());
            st.executeUpdate();

            st.close();
            return liked;
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<Recipe> getLikedRecipesByUser(User user) {
        final List<Recipe> recipes = new ArrayList<>();

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM liked WHERE userID = ?");
            st.setInt(1, user.getUserID());
            final ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                final int recipeID = rs.getInt("recipeID");
                RecipePersistence recipePersistence = Services.getRecipePersistence();
                final Recipe recipe = recipePersistence.getRecipeByID(recipeID);
                recipes.add(recipe);
            }
            rs.close();
            st.close();

            return recipes;
        }
        catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }

    @Override
    public List<User> getUserLikesByRecipe(Recipe recipe) {
        final List<User> users = new ArrayList<>();

        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("SELECT * FROM liked WHERE recipeID = ?");
            st.setInt(1, recipe.getRecipeID());
            final ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                final int userID = rs.getInt("userID");
                UserPersistence userPersistence = Services.getUserPersistence();
                final User user = userPersistence.getUser(userID);
                users.add(user);
            }
            rs.close();
            st.close();

            return users;
        }
        catch (final SQLException e)
        {
            throw new PersistenceException(e);
        }
    }

    @Override
    public void deleteLiked(Liked liked) {
        try (final Connection c = connection()) {
            final PreparedStatement st = c.prepareStatement("DELETE FROM liked WHERE userID = ? AND recipeID = ?");
            st.setInt(1, liked.getUser().getUserID());
            st.setInt(2, liked.getRecipe().getRecipeID());
            st.executeUpdate();
            st.close();
        } catch (final SQLException e) {
            throw new PersistenceException(e);
        }
    }
}
