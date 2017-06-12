package com.example.leon.article.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.leon.article.Activity.NoticeActivity;
import com.example.leon.article.Activity.art.ArtConstant;
import com.example.leon.article.Activity.art.ArtDetailActivity;
import com.example.leon.article.R;
import com.example.leon.article.bean.AdvBean;
import com.example.leon.article.bean.ExcellentBean;
import com.example.leon.article.bean.NoticeBean;
import com.example.leon.article.utils.Constant;
import com.example.leon.article.widget.MarqueTextView;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.List;

/**
 * Created by leonseven on 2017/5/26.
 */

public class HomeRecycleAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String headurl = Constant.Api.BASE_URL;

    private static Context context;

    List<AdvBean.DataBean> DatabeanList;
//    List<RecomArtBean.DataBean.TuijianBean> reBeanlist;
    static List<ExcellentBean.DataBean.GoodBean> reBeanlist;
    List<NoticeBean.DataBean> noticeBean;

    //type
    public static final int TYPE_HEAD1 = 0xff01;
    public static final int TYPE_HEAD2 = 0xff02;
    public static final int TYPE_TYPE3_FRAGMENT = 0xff03;

    public HomeRecycleAdapter2(List<AdvBean.DataBean> list, List<ExcellentBean.DataBean.GoodBean> Goodlist,
                               List<NoticeBean.DataBean> noticeBean
                              , Context context) {
        this.DatabeanList = list;
        this.reBeanlist = Goodlist;
        this.noticeBean = noticeBean;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){

            case TYPE_HEAD1:
                return new HolderTypeHead1(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homefragment_head1, parent, false));
            case TYPE_HEAD2:
                return new HolderTypeHead2(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homefragment_head2, parent, false));
            case TYPE_TYPE3_FRAGMENT:
                return new HolderTypeArticle3(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article_recyclerview, parent, false));
            default:
                Log.d("error","viewholder is null");
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof HolderTypeHead1){
            bindTypeHead1((HolderTypeHead1) holder, position);
        }
        else if (holder instanceof HolderTypeHead2){
            bindTypeHead2((HolderTypeHead2) holder, position);
        }
        else if (holder instanceof HolderTypeArticle3){
            bindTypeArticle3((HolderTypeArticle3) holder, position);
        }
    }

    @Override
    public int getItemCount() {
            return reBeanlist.size()+2;

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return TYPE_HEAD1;
        }else if (position == 1){
            return TYPE_HEAD2;
        }else {
            return TYPE_TYPE3_FRAGMENT;
        }
    }

    /**
     * bind HEAD1 轮播图
     */
    private void bindTypeHead1(HolderTypeHead1 holder, int position){

        holder.rollpagerview.setAnimationDurtion(500);    //设置切换时间
        holder.rollpagerview.setAdapter(new TestLoopAdapter(holder.rollpagerview, DatabeanList)); //设置适配器
        holder.rollpagerview.setHintView(new ColorPointHintView(context, Color.WHITE, Color.GRAY));// 设置圆点指示器颜色

    }

    private void bindTypeHead2(final HolderTypeHead2 holder, final int position){

        String text = "";
        for (int i = 0; i<noticeBean.size(); i++){
            String a = "";
            a = noticeBean.get(i).getNtitle();
            text = "   " + text +  "   " + a + "    ";
        }

       if (noticeBean.size()>0){
           holder.text_notice.setText(text);
       }
       else {}

        //Intent 消息Activity
        holder.lin_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context. startActivity(new Intent(context, NoticeActivity.class));
            }
        });

        holder.lin_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //发送广播
                Intent intent = new Intent();
                intent.setAction("article1");
                context.sendBroadcast(intent);

                HolderTypeArticle3.lin1.setVisibility(View.VISIBLE);
                HolderTypeArticle3.lin2.setVisibility(View.GONE);
            }
        });

        holder.lin_excellent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HolderTypeArticle3.lin1.setVisibility(View.GONE);
                HolderTypeArticle3.lin2.setVisibility(View.VISIBLE);
            }
        });
    }

    //文章
    private void bindTypeArticle3(final HolderTypeArticle3 holder, final int position){

        if (reBeanlist.size()>0) {

            if (position == 2){
                holder.text_contect.setTextColor(context.getResources().getColor(R.color.colorRed));
                holder.text_date.setTextColor(context.getResources().getColor(R.color.colorRed));
            }
            else {
                holder.text_contect.setTextColor(context.getResources().getColor(R.color.textcolorbalck));
                holder.text_date.setTextColor(context.getResources().getColor(R.color.textcolorbalck));
            }


            holder.text_contect.setText(reBeanlist.get(position - 2).getAtitle());
            holder.text_date.setText(reBeanlist.get(position - 2).getAaddtime());

            if (reBeanlist.get(position - 2).getAimg()==null||reBeanlist.get(position - 2).getAimg().equals("")){
                Glide.with(context)
                        .load(R.drawable.adv2)
                        .centerCrop()
                        .into(holder.img);
            }
            else {
                Glide.with(context)
                        .load(headurl + reBeanlist.get(position - 2).getAimg())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .centerCrop()
                        .into(holder.img);
            }

        }

    }



    /**
     * Head1 轮播图
     */
    public class HolderTypeHead1 extends RecyclerView.ViewHolder {
        public RollPagerView rollpagerview;

        public HolderTypeHead1(View itemView) {
            super(itemView);
            rollpagerview = (RollPagerView) itemView.findViewById(R.id.rollpagerview);
        }
    }

    /**
     * Head2 SystemMessage
     */
    public class HolderTypeHead2 extends RecyclerView.ViewHolder {
        public LinearLayout lin_notice;
        public MarqueTextView text_notice;

        public LinearLayout lin_recommend;
        public LinearLayout lin_excellent;

        public TextView textveiw_good;
        public TextView textveiw_recommend;
        public ImageView imagview_good;
        public ImageView imagview_recommend;
        public HolderTypeHead2(View itemView) {
            super(itemView);
            lin_notice = (LinearLayout) itemView.findViewById(R.id.lin_notice);
            text_notice = (MarqueTextView) itemView.findViewById(R.id.text_notice);
            lin_recommend = (LinearLayout) itemView.findViewById(R.id.lin_recommend);
            lin_excellent = (LinearLayout) itemView.findViewById(R.id.lin_excellent);
            textveiw_good = (TextView) itemView.findViewById(R.id.textveiw_good);
            textveiw_recommend = (TextView) itemView.findViewById(R.id.textveiw_recommend);
            imagview_good = (ImageView) itemView.findViewById(R.id.imagview_good);
            imagview_recommend = (ImageView) itemView.findViewById(R.id.imagview_recommend);

            lin_recommend.setEnabled(true);
            lin_excellent.setEnabled(false);

            imagview_recommend.setSelected(false);
            imagview_good.setSelected(true);
            textveiw_good.setTextColor(context.getResources().getColor(R.color.colorRed));
            textveiw_recommend.setTextColor(context.getResources().getColor(R.color.colorGrey));
        }
    }

    /**
     * Head3 SystemArticle
     */
    public static class HolderTypeArticle3 extends RecyclerView.ViewHolder {
        public TextView text_contect;
        public TextView text_date;
        public ImageView img;
        public static LinearLayout lin1;

        public TextView text_contect2;
        public TextView text_date2;
        public ImageView img2;
        public static LinearLayout lin2;

        public HolderTypeArticle3(View itemView) {
            super(itemView);
            text_contect = (TextView) itemView.findViewById(R.id.text_contect);
            text_date = (TextView) itemView.findViewById(R.id.text_date);
            img = (ImageView) itemView.findViewById(R.id.img);
            lin1 = (LinearLayout) itemView.findViewById(R.id.lin1);

            text_contect2 = (TextView) itemView.findViewById(R.id.text_contect2);
            text_date2 = (TextView) itemView.findViewById(R.id.text_date2);
            img2 = (ImageView) itemView.findViewById(R.id.img2);
            lin2 = (LinearLayout) itemView.findViewById(R.id.lin2);

            initListener(itemView);
        }

        private void initListener(View itemView) {
            lin1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.i("MyTest", "poistion"+getAdapterPosition());
                    String aid = reBeanlist.get(getAdapterPosition() - 2).getAid();
                    Intent intent = new Intent(context, ArtDetailActivity.class);
                    intent.putExtra(ArtConstant.DETAIL_AID, aid);
                    context.startActivity(intent);
                }
            });
        }

    }

    public void addNotItems(List<NoticeBean.DataBean> List){
        noticeBean.addAll(List);
        notifyDataSetChanged();
    }
//    public void addArtItems(List<RecomArtBean.DataBean.TuijianBean> List){
//        reBeanlist.addAll(List);
//        notifyDataSetChanged();
//    }

    public void addGoodItems(List<ExcellentBean.DataBean.GoodBean> List){
        if (List.size()>0){
            reBeanlist.addAll(List);
        }
        notifyDataSetChanged();
    }

    public void addAdvItems(List<AdvBean.DataBean> List){
        DatabeanList.addAll(List);
        notifyDataSetChanged();
    }


    /**
     * 轮播图adapter
     */
    private class TestLoopAdapter extends LoopPagerAdapter {

        List<AdvBean.DataBean> list;

//        private int[] imgs = {R.drawable.adv, R.drawable.goto_article, R.drawable.adv, R.drawable.available_balance,};  // 本地图片
//        private int count = imgs.length;  // banner上图片的数量

        public TestLoopAdapter(RollPagerView viewPager, List<AdvBean.DataBean> list) {
            super(viewPager);
            this.list = list;
        }

        @Override
        public View getView(ViewGroup container, int position) {

            final int picNo = position + 1;
            ImageView view = new ImageView(container.getContext());

            Glide.with(context)
                    .load(headurl+list.get(position).getImg())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .into(view);
//            view.setImageResource(list[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            view.setOnClickListener(new View.OnClickListener()      // 点击事件
            {
                @Override
                public void onClick(View v)
                {
//                    Toast.makeText(context, "点击了第" + picNo + "张图片", Toast.LENGTH_SHORT).show();
                }

            });

            return view;
        }

        @Override
        public int getRealCount() {
            return list.size();
        }
    }

}
