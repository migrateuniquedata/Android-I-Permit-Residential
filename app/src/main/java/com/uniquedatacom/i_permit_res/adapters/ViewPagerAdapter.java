package com.uniquedatacom.i_permit_res.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.uniquedatacom.i_permit_res.activities.OnceFragment;
import com.uniquedatacom.i_permit_res.activities.PermitFragment;

public class ViewPagerAdapter extends SmartFragmentStatePagerAdapter
{

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior)
    {
        super(fm,behavior);
    }

    @Override
    public Fragment getItem(int position)
    {
        switch(position)
        {
            case 0:
                return new OnceFragment();
            case 1:
                return new PermitFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount()
    {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        switch(position)
        {
            case 0:
                return "Once";
            case 1:
                return "Permit";

            default:
                return null;
        }
    }
}
