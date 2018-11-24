package android.example.com.split;

import android.content.Context;
import android.content.Intent;
import android.example.com.split.data.entity.Group;
import android.support.annotation.CallSuper;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.io.Serializable;

public abstract class BaseViewHolder<T extends Serializable> extends RecyclerView.ViewHolder
    implements View.OnClickListener {

  private final View itemView;
  private final Class<? extends FragmentActivity> detailActivityClass;
  private final String title;
  private T itemData;
  private OnDeleteItemListener onDeleteItemListener;

  public BaseViewHolder(View itemView, Class<? extends FragmentActivity> detailActivityClass,
                        String title) {
    super(itemView);
    this.detailActivityClass = detailActivityClass;
    this.itemView = itemView;
    this.title = title;
    itemView.setOnClickListener(this);
    findAllViews(itemView);
  }

  protected abstract void findAllViews(View itemView);

  @CallSuper
  public void bind(T t) {
    setItemData(t);
  }

  public abstract void bind(Group group, T expense, int position);

  public abstract void onItemClicked(View itemView);

  public View getItemView() {
    return itemView;
  }

  public void setOnDeleteItemListener(OnDeleteItemListener onDeleteItemListener) {
    this.onDeleteItemListener = onDeleteItemListener;
  }

  protected void deleteItem(int position) {
    onDeleteItemListener.onDelete(position);
  }

  @Override
  public void onClick(View v) {
    onItemClicked(v);
  }

  public T getItemData() {
    return itemData;
  }

  protected void setItemData(T t) {
    itemData = t;
  }

  public Class<?> getDetailActivityClass() {
    return detailActivityClass;
  }

  protected void startDetailActivity(Context context) {
    Intent intent = new Intent(context, getDetailActivityClass());
    intent.putExtra(title, getItemData());
    context.startActivity(intent);
  }
}
