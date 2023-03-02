package comp3350.acci.presentation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.Stack;

import comp3350.acci.R;

public class FragmentNavigator {

    private Stack<Fragment> fragmentHistory = new Stack<>();

    private FragmentManager manager;

    public FragmentNavigator(FragmentManager supportFragmentManger){
        manager = supportFragmentManger;
    }

    // Updates the FragmentManager with the current fragment
    private boolean updateManager(){
        if(fragmentHistory.size() > 1){
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            fragmentTransaction.replace(R.id.current_fragment, currentFragment());
            fragmentTransaction.commit();
            return true;
        }
        return false;
    }

    public Fragment currentFragment(){
        return fragmentHistory.peek();
    }

    public boolean setFragment(Fragment f){
        fragmentHistory.push(f);
        return updateManager();
    }

    public boolean undoFragment(){
        fragmentHistory.pop();
        return updateManager();
    }

    public void clear(){
        fragmentHistory.clear();
    }


}
