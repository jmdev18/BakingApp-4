package com.wolfgoes.bakingapp.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.wolfgoes.bakingapp.R;

import java.text.DecimalFormat;

public class Ingredient implements Parcelable {

    public double quantity;
    public Measure measure;
    public String ingredient;

    enum Measure {
        CUP,
        TBLSP,
        TSP,
        K,
        G,
        OZ,
        UNIT
    }

    protected Ingredient(Parcel in) {
        quantity = in.readDouble();
        measure = (Measure) in.readSerializable();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(quantity);
        parcel.writeSerializable(measure);
        parcel.writeString(ingredient);
    }

    public String getString(Context context) {
        DecimalFormat format = new DecimalFormat("0.#");

        String result = format.format(quantity);

        switch (measure) {
            case CUP:
                result += " " + context.getResources().getString(R.string.cup);
                break;
            case TBLSP:
                result += " " + context.getResources().getString(R.string.tablespoon);
                break;
            case TSP:
                result += " " + context.getResources().getString(R.string.teaspoon);
                break;
            case K:
                result += context.getResources().getString(R.string.kilogram);
                break;
            case G:
                result += context.getResources().getString(R.string.gram);
                break;
            case OZ:
                result += context.getResources().getString(R.string.ounce);
                break;
            case UNIT:
                //add nothing
                break;
        }

        result += " " + ingredient;

        return result;
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
