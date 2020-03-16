package cn.zmmax.zebar.adapter;

import com.xuexiang.xpage.model.PageInfo;

import java.util.List;

import cn.zmmax.zebar.R;

/**
 * 菜单适配器
 */
public class WidgetItemAdapter extends BaseRecyclerAdapter<PageInfo> {

    public WidgetItemAdapter(List<PageInfo> list) {
        super(list);
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.layout_widget_item;
    }

    @Override
    public void bindData(RecyclerViewHolder holder, int position, PageInfo item) {
        holder.getTextView(R.id.item_name).setText(item.getName());
        if (item.getExtra() != -1) {
            holder.getImageView(R.id.item_icon).setImageResource(item.getExtra());
        }
    }
}
