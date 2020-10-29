package com.babbangona.mspalybookupgrade.RecyclerAdapters.FertilizerSignUpHomeRecycler;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom item decoration for a vertical {@link LeadersGridDecoration} {@link RecyclerView}. Adds a
 * small amount of padding to the left of grid items, and a large amount of padding to the right.
 */
public class LeadersGridDecoration extends RecyclerView.ItemDecoration {
    private int largePadding;
    private int smallPadding;

    public LeadersGridDecoration(int largePadding, int smallPadding) {
        this.largePadding = largePadding;
        this.smallPadding = smallPadding;

        /*
        recycler_view.setLayoutManager(new GridLayoutManager(TFMHome.this, 2, RecyclerView.VERTICAL, false));
        recycler_view.setLayoutManager(new GridLayoutManager(TFMHome.this, 2, RecyclerView.VERTICAL, false));
        int largePadding = getResources().getDimensionPixelSize(R.dimen.tfm_home_grid_spacing_small);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.tfm_home_grid_spacing_small);
        recycler_view.addItemDecoration(new LeadersGridDecoration(largePadding, smallPadding));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        recycler_view.addItemDecoration(dividerItemDecoration);*/
    }

    @Override
    public void getItemOffsets(Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = smallPadding;
        outRect.right = smallPadding;
        outRect.top = largePadding;
        outRect.bottom = largePadding;
    }
}
