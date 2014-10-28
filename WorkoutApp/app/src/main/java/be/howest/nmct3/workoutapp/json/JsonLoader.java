package be.howest.nmct3.workoutapp.json;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

/**
 * Created by nielslammens on 28/10/14.
 */
public abstract class JsonLoader extends AsyncTaskLoader<Cursor>{

    private final String[] mColumnNames;
    private final String mPropertyName;
    private final int mRawResourceId;

    private Cursor mCursor;
    private Object lock = new Object();

    public JsonLoader(Context context, String propertyName, String[] columnNames, int rawResourceId) {
        super(context);
        mPropertyName = propertyName;
        mColumnNames = columnNames;
        mRawResourceId = rawResourceId;
    }

    @Override
    protected void onStartLoading() {
        if (mCursor != null){
            deliverResult(mCursor);
        }

        if(takeContentChanged() || mCursor == null){
            forceLoad();
        }
    }

    @Override
    public Cursor loadInBackground() {
        if(mCursor == null){
            loadCursor();
        }

        return mCursor;
    }

    private void loadCursor(){
        synchronized (lock){
            if (mCursor != null) return;

            MatrixCursor cursor = new MatrixCursor(mColumnNames);
            InputStream in = getContext().getResources().openRawResource(mRawResourceId);

            JsonReader reader = new JsonReader(new InputStreamReader(in));
            try{
                reader.beginObject();
                while (reader.hasNext()){
                    String propName = reader.nextName();
                    if(propName.equals(mPropertyName)){
                        parse(reader, cursor);
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            }catch (IOException ex){

            }finally {
                try { reader.close(); } catch (IOException e) {}
                try { in.close(); } catch(IOException e) {}
            }
            mCursor = cursor;
        }
    }

    protected abstract void parse(JsonReader reader, MatrixCursor cursor) throws IOException;
}
