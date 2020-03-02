package com.odelan.yang.aggone.Activity.Profile;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.odelan.yang.aggone.Adapter.PagerAdapter;
import com.odelan.yang.aggone.Fragment.Description.IndividualTechniqueFragment;
import com.odelan.yang.aggone.Fragment.Description.PhysicalQuantitiesFragment;
import com.odelan.yang.aggone.Fragment.Description.SkillsFragment;
import com.odelan.yang.aggone.Fragment.Description.TacticsFragment;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditDescriptionActivity extends AppCompatActivity {

    User user;

    @BindView(R.id.view_pager) ViewPager view_pager;
    PagerAdapter adapter;
    Fragment[] fragments;

    SkillsFragment.Listener skillsListener = new SkillsFragment.Listener() {
    };

    IndividualTechniqueFragment.Listener individualTechniqueListener = new IndividualTechniqueFragment.Listener() {
    };

    PhysicalQuantitiesFragment.Listener physicalQuantitiesListener = new PhysicalQuantitiesFragment.Listener() {
    };

    TacticsFragment.Listener tacticsListener = new TacticsFragment.Listener() {
    };

    @BindView(R.id.txt_skills) TextView txt_skills;
    @BindView(R.id.txt_individual_technique) TextView txt_individual_technique;
    @BindView(R.id.txt_physical_quantities) TextView txt_physical_quantities;
    @BindView(R.id.txt_tactics) TextView txt_tactics;

    @BindView(R.id.img_skills) ImageView img_skills;
    @BindView(R.id.img_individual_technique) ImageView img_individual_technique;
    @BindView(R.id.img_physical_quantities) ImageView img_physical_quantities;
    @BindView(R.id.img_tactics) ImageView img_tactics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        ButterKnife.bind(this);

        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    void setActivity() {
        if (user.group_id == Constants.PLAYER) {
            txt_skills.setText(getString(R.string.skill1));
            txt_individual_technique.setText(R.string.skill2);
            txt_physical_quantities.setText(R.string.skill3);
            txt_tactics.setText(R.string.skill4);
        } else {
            txt_skills.setText(getString(R.string.cskill1));
            txt_individual_technique.setText(R.string.cskill2);
            txt_physical_quantities.setText(R.string.cskill3);
            txt_tactics.setText(R.string.cskill4);
        }
        clearTab();
        fragments = new Fragment[] {
                new SkillsFragment(this, user, skillsListener),
                new IndividualTechniqueFragment(this, user, individualTechniqueListener),
                new PhysicalQuantitiesFragment(this, user, physicalQuantitiesListener),
                new TacticsFragment(this, user, tacticsListener),
        };
        adapter = new PagerAdapter(this, fragments, getSupportFragmentManager());
        view_pager.setAdapter(adapter);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }
            @Override
            public void onPageSelected(int i) {
                setTabTitle(i);
            }
            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
        view_pager.setCurrentItem(0);
        setTabTitle(0);
    }

    void setTabTitle(int index) {
        clearTab();
        if (index == 0) {
            txt_skills.setAlpha(1.0f);
            img_skills.setVisibility(View.VISIBLE);
        } else if (index == 1) {
            txt_individual_technique.setAlpha(1.0f);
            img_individual_technique.setVisibility(View.VISIBLE);
        } else if (index == 2) {
            txt_physical_quantities.setAlpha(1.0f);
            img_physical_quantities.setVisibility(View.VISIBLE);
        } else if (index == 3) {
            txt_tactics.setAlpha(1.0f);
            img_tactics.setVisibility(View.VISIBLE);
        }
    }

    void clearTab() {
        txt_skills.setAlpha(0.5f);
        txt_individual_technique.setAlpha(0.5f);
        txt_physical_quantities.setAlpha(0.5f);
        txt_tactics.setAlpha(0.5f);
        img_skills.setVisibility(View.INVISIBLE);
        img_individual_technique.setVisibility(View.INVISIBLE);
        img_physical_quantities.setVisibility(View.INVISIBLE);
        img_tactics.setVisibility(View.INVISIBLE);
    }

    @OnClick(R.id.txt_skills) void onClickSkills() {
        view_pager.setCurrentItem(0);
    }
    @OnClick(R.id.txt_individual_technique) void onClickIndividualTechnique() {
        view_pager.setCurrentItem(1);
    }
    @OnClick(R.id.txt_physical_quantities) void onClickPhysicalQuantities() {
        view_pager.setCurrentItem(2);
    }
    @OnClick(R.id.txt_tactics) void onClickTactics() {
        view_pager.setCurrentItem(3);
    }

    @OnClick(R.id.btn_back) void onClickBack() {
        finish();
    }

    @OnClick(R.id.btn_check) void onClickCheck() { finish(); }
}
