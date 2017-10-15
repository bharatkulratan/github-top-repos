package info.techienotes.toprepos.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import info.techienotes.toprepos.HomeActivity;
import info.techienotes.toprepos.R;
import info.techienotes.toprepos.model.Repo;

/**
 * Created by bharatkulratan
 */

public class RepoItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    implements View.OnClickListener{

    private final List<Repo> itemList;
    private final HomeActivity homeActivity;

    public RepoItemAdapter( HomeActivity homeActivity, List<Repo> itemList){
        this.itemList = itemList;
        this.homeActivity = homeActivity;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new RepoItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Repo item = itemList.get(position);
        RepoItemHolder viewHolder = (RepoItemHolder) holder;

        viewHolder.getRepoFullName().setText(item.getName());
        viewHolder.getRepoLanguage().setText(item.getLanguage());

        if (!TextUtils.isEmpty(item.getDescription())) {
            viewHolder.getDescription().setText(item.getDescription());
        }

        viewHolder.getStarGazersCount().setText(String.valueOf(item.getStarCount()));
        viewHolder.getForkCount().setText(String.valueOf(item.getForkCount()));

        String authorName = homeActivity.getString(R.string.author_name, item.getAuthor());
        viewHolder.getAuthor().setText(authorName);

        viewHolder.getMainLayout().setTag(position);
        viewHolder.getMainLayout().setOnClickListener(this);
    }

    class RepoItemHolder extends RecyclerView.ViewHolder{
        public RepoItemHolder(View view){
            super(view);

            mainLayout = (RelativeLayout)view.findViewById(R.id.main_layout);
            repoFullName = (TextView)view.findViewById(R.id.tv_repo_header);
            description = (TextView)view.findViewById(R.id.tv_repo_description);
            repoLanguage = (TextView)view.findViewById(R.id.tv_language);
            starGazersCount = (TextView)view.findViewById(R.id.tv_star_info);
            forkCount = (TextView)view.findViewById(R.id.tv_fork_info);
            author = (TextView)view.findViewById(R.id.tv_repo_user);

        }

        private TextView repoFullName;
        private TextView repoLanguage;
        private TextView starGazersCount;
        private TextView forkCount;
        private TextView author;
        private RelativeLayout mainLayout;
        private TextView description;

        private TextView getRepoFullName() {
            return repoFullName;
        }

        private TextView getRepoLanguage() {
            return repoLanguage;
        }

        private TextView getStarGazersCount() {
            return starGazersCount;
        }

        private TextView getAuthor() {
            return author;
        }

        private RelativeLayout getMainLayout() {
            return mainLayout;
        }

        private TextView getDescription() {
            return description;
        }

        private TextView getForkCount() {
            return forkCount;
        }

    }

    @Override
    public void onClick(View v) {

        if (itemList == null || itemList.isEmpty()){
            return;
        }

        int cardPosition = (int)v.getTag();

        homeActivity.onItemClicked(cardPosition);
    }
}
