package com.honestwalker.android.commons.menu;

import android.app.Activity;
import android.support.v4.app.Fragment;

import com.honestwalker.androidutils.activity.fragment.menubar.MenubarItemBean;

/**
 *
 * Created by lan zhe on 15-9-24.
 */
public class BaseTabIconFragment extends Fragment {

    protected Activity context;

    private boolean isTricky;

    private MenubarItemBean menubarItemBean;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    {
//        readAnnotation(this);
    }

    private void readAnnotation(BaseTabIconFragment fragment){

//        FragmentMenu menu = fragment.getClass().getAnnotation(FragmentMenu.class);
//        if(menu != null) {
//
//        } else {
//            throw new RuntimeException("fragment 未配置");
//        }

        TrickyFragment tricky = fragment.getClass().getAnnotation(TrickyFragment.class);
        if(tricky != null){
            isTricky = true;
//            title = tricky.trickyTitle();
        }
    }

    public boolean isTricky() {
        return isTricky;
    }

    public MenubarItemBean getMenubarItemBean() {
        return menubarItemBean;
    }

    public void setMenubarItemBean(MenubarItemBean menubarItemBean) {
        this.menubarItemBean = menubarItemBean;
    }

}
