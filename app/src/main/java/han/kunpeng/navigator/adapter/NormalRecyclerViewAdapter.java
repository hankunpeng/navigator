package han.kunpeng.navigator.adapter;

import android.content.ClipData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import han.kunpeng.navigator.R;

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private final Context mContext;
//    private String[] mTitles;
    private String[] mTitles = {"1", "2", "3"};

    public NormalRecyclerViewAdapter(Context context) {
//        mTitles = context.getResources().getStringArray(R.array.titles);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

/*
    private OnItemClickListener mOnItemClickListener = null;

    // define interface
    public static interface OnItemClickListener {
        void onItemClick(View view , int position);
    }
*/

    // 初始化自定义 ViewHolder 时调用此方法
    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.mTextView.setText(mTitles[position]);
    }

    // 返回数据中现存的条目数
    @Override
    public int getItemCount() {
        return mTitles == null ? 0 : mTitles.length;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder{
//        public CardView mCardView;
        TextView mTextView;

        NormalTextViewHolder(View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                }
            });
            mTextView = view.findViewById(R.id.text_view);
        }}
}
