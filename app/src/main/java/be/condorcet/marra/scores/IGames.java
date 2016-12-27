package be.condorcet.marra.scores;

import android.content.Context;

import java.util.ArrayList;

public interface IGames  {

    public void responseAsync(ArrayList<String> result, int code);
    public Context getContext();
}
