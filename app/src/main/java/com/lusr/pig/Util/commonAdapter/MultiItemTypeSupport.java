package com.lusr.pig.Util.commonAdapter;

public interface MultiItemTypeSupport<T>
{
    int getLayoutId(int itemType);
    int getItemViewType(int position, T t);
}