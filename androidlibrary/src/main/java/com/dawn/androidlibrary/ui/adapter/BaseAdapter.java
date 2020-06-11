package com.dawn.androidlibrary.ui.adapter;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 默认适配器
 * @param <T> item显示的实体类
 *           必须设置的属性
 *           recyclerView.setLayoutManager(new LinearLayoutManager(this));
 */
public abstract class BaseAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    public BaseAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public void setOnItemChildClickListener(final OnItemChildClickListener<T> listener){
        super.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                T t = null;
                try{
                    //noinspection unchecked
                    t = ((List<T>)adapter.getData()).get(position);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(listener != null)
                    listener.onItemChildClick(t, view, position);
            }
        });
    }

    public interface OnItemChildClickListener<T>{
        void onItemChildClick(T data, View view, int position);
    }
}
