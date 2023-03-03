package comp3350.acci.presentation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import comp3350.acci.R;
import comp3350.acci.databinding.ActivityMainBinding;
import comp3350.acci.presentation.discovery.DiscoveryActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private FragmentNavigator fragmentNavigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());// Set app display to this file

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        // Set starting fragment
        fragmentNavigator = new FragmentNavigator(this.getSupportFragmentManager());
        fragmentNavigator.setFragment(new DiscoveryActivity(this));

        binding.navigationBar.setOnItemSelectedListener(item -> {
            fragmentNavigator.clear();
            showNavigationBar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            switch (item.getItemId()){
                case R.id.menu_discovery:
                    fragmentNavigator.setFragment(new DiscoveryActivity(this));
                    break;
                case R.id.menu_insert_recipe:
                    fragmentNavigator.setFragment(new InsertRecipeActivity(this));
                    break;
                /*case R.id.menu_profile:
                    replaceFragment(new ProfileActivity());
                    break;*/
            }
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Pop stack, switching between THESE menus should clear the history as the fragmentNavigator is used to handle submenus/subfragments
        // Make current top, current fragment
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "Back Button!", Toast.LENGTH_SHORT).show();
                fragmentNavigator.undoFragment();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Public function to be used outside this class
    public boolean setFragment(Fragment f){
        return fragmentNavigator.setFragment(f);
    }


    // To hide the nav bar in a constraint layout, we need to do 2 things
    // 1. Make the navigation bar invisible: set the height to 0.
    // 2. Make the fragment fullscreen/take its place: re-wire the constraint; tie the fragment to the bottom of the screen (instead of the top of the nav bar.)
    public void hideNavigationBar(){
        BottomNavigationView bar = binding.navigationBar;
        FragmentContainerView fragment = binding.currentFragment;
        ConstraintLayout layout = binding.container;

        bar.getLayoutParams().height = 0;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(fragment.getId(),ConstraintSet.BOTTOM,layout.getId(),ConstraintSet.BOTTOM,0);
        constraintSet.applyTo(layout);
    }

    // Reset the height and constraints.
    public void showNavigationBar(){
        BottomNavigationView bar = binding.navigationBar;
        FragmentContainerView fragment = binding.currentFragment;
        ConstraintLayout layout = binding.container;

        bar.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(layout);
        constraintSet.connect(fragment.getId(),ConstraintSet.BOTTOM, bar.getId(),ConstraintSet.TOP,0);
        constraintSet.applyTo(layout);

        //Toast.makeText(this, "SHOW NAV!!", Toast.LENGTH_SHORT).show();
    }


}
