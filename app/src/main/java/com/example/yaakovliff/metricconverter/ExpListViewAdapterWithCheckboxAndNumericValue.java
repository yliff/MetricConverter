package com.example.yaakovliff.metricconverter;

/**
 * Created by dbhat on 15-03-2016.
 * Modified by SA on 5/10/16
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Eclipse wanted me to use a sparse array instead of my hashmaps, I just suppressed that suggestion
@SuppressLint ("UseSparseArrays")
public class ExpListViewAdapterWithCheckboxAndNumericValue extends BaseExpandableListAdapter
{

    // Define activity context
    private Context mContext;

    /*
     * HashMap for Strings and for Numeric Values (Integers)
    */
    private HashMap<String, List<String>> mListStringDataChild;
    private HashMap<String, List<Integer>> mListNumericDataChild;

    // ArrayList which is what each key in the above HashMaps point to
    private ArrayList<String> mListDataGroup;

    // Hashmap for keeping track of our checkbox check states
    private HashMap<Integer, boolean[]> mChildCheckStates;


    // Our getChildView & getGroupView use the viewholder pattern
    // Here are the viewholders defined, with the inner classes at the bottom
    private ChildViewHolder childViewHolder;
    private GroupViewHolder groupViewHolder;

    /* Each group child item contains both a name and a number
    */
    private String strGroupName;
    private String strChildName;
    private int intChildValue;

    /*  Here's the constructor we'll use to pass in our calling
     *  activity's context, group items, and child items
    */
    public ExpListViewAdapterWithCheckboxAndNumericValue (Context context, ArrayList<String> listDataGroup,
                                                          HashMap<String, List<String>> listStringDataChild,
                                                          HashMap<String, List<Integer>> listNumericDataChild)
    {

        mContext = context;
        mListDataGroup = listDataGroup;
        mListStringDataChild = listStringDataChild;
        mListNumericDataChild = listNumericDataChild;

        // Initialize our hashmap containing our check states here
        mChildCheckStates = new HashMap<Integer, boolean[]> ();
    }

    public int getNumberOfCheckedItemsInGroup (int mGroupPosition)
    {
        boolean getChecked[] = mChildCheckStates.get (mGroupPosition);
        int count = 0;
        if (getChecked != null) {
            for (int j = 0; j < getChecked.length; ++j) {
                if (getChecked[j] == true) {
                    count++;
                }
            }
        }
        return count;
    }

    public int getSumOfCheckedItemsInGroup (int groupPosition)
    {
        boolean getChecked[] = mChildCheckStates.get (groupPosition);
        int sum = 0;
        if (getChecked != null) {
            for (int j = 0; j < getChecked.length; ++j) {
                if (getChecked[j] == true) {
                    sum+= getChildNumericData(groupPosition, j);
                }
            }
        }
        return sum;
    }


    @Override
    public int getGroupCount ()
    {
        return mListDataGroup.size ();
    }

    /*
     * This defaults to "public object getGroup" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public String getGroup (int groupPosition)
    {
        return mListDataGroup.get (groupPosition);
    }

    @Override
    public long getGroupId (int groupPosition)
    {
        return groupPosition;
    }


    @Override
    public View getGroupView (int groupPosition, boolean isExpanded,
                              View convertView, ViewGroup parent)
    {

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListGroupItems".
        //  Here is where I call the getter to get that text
        strGroupName = getGroup (groupPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate (R.layout.list_group, null);

            // Initialize the GroupViewHolder defined at the bottom of this document
            groupViewHolder = new GroupViewHolder ();

            groupViewHolder.mGroupText = (TextView) convertView.findViewById (R.id.lblListHeader);

            convertView.setTag (groupViewHolder);
        }
        else {

            groupViewHolder = (GroupViewHolder) convertView.getTag ();
        }

        groupViewHolder.mGroupText.setText (strGroupName);

        return convertView;
    }

    @Override
    public int getChildrenCount (int groupPosition)
    {
        return mListStringDataChild.get (mListDataGroup.get (groupPosition)).size ();
    }

    public int getSumOfChildrenNumericValues (int groupPosition)
    {
        int sum = 0;
        for (int currentChildValue : mListNumericDataChild.get (
                                            mListDataGroup.get (groupPosition))) {
            sum+=currentChildValue;
        }
        return sum;
    }

    /*
     * This defaults to "public object getChild" if you auto import the methods
     * I've always make a point to change it from "object" to whatever item
     * I passed through the constructor
    */
    @Override
    public String getChild (int groupPosition, int childPosition)
    {
        return mListStringDataChild.get (mListDataGroup.get (groupPosition)).get (childPosition);
    }

    public int getChildNumericData (int groupPosition, int childPosition)
    {
        return mListNumericDataChild.get (mListDataGroup.get (groupPosition)).get (childPosition);
    }

    @Override
    public long getChildId (int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView (int groupPosition, int childPosition, boolean isLastChild,
                              View convertView, ViewGroup parent)
    {

        final int mGroupPosition = groupPosition;
        final int mChildPosition = childPosition;

        //  I passed a text string into an activity holding a getter/setter
        //  which I passed in through "ExpListChildItems".
        //  Here is where I call the getter to get that text
        strChildName = getChild (mGroupPosition, mChildPosition);
        intChildValue = getChildNumericData (mGroupPosition, mChildPosition);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) this.mContext
                    .getSystemService (Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate (R.layout.list_item, parent, false);

            childViewHolder = new ChildViewHolder ();

            childViewHolder.mChildStringText = (TextView) convertView
                    .findViewById (R.id.lblListItem);

            childViewHolder.mChildNumberText = (TextView) convertView
                    .findViewById (R.id.lblListItemNumberValue);

            childViewHolder.mCheckBox = (CheckBox) convertView
                    .findViewById (R.id.cbListItem);

            convertView.setTag (R.layout.list_item, childViewHolder);

        }
        else {

            childViewHolder = (ChildViewHolder) convertView
                    .getTag (R.layout.list_item);
        }

        childViewHolder.mChildStringText.setText (strChildName);
        childViewHolder.mChildNumberText.setText (String.valueOf (intChildValue));

		/*
         * You have to set the onCheckChangedListener to null
		 * before restoring check states because each call to
		 * "setChecked" is accompanied by a call to the
		 * onCheckChangedListener
		*/
        childViewHolder.mCheckBox.setOnCheckedChangeListener (null);

        if (mChildCheckStates.containsKey (mGroupPosition)) {
			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> contains
			 * the value of the parent view (group) of this child (aka, the key),
			 * then retrive the boolean array getChecked[]
			*/
            boolean getChecked[] = mChildCheckStates.get (mGroupPosition);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.mCheckBox.setChecked (getChecked[mChildPosition]);

        }
        else {

			/*
			 * if the hashmap mChildCheckStates<Integer, Boolean[]> does not
			 * contain the value of the parent view (group) of this child (aka, the key),
			 * (aka, the key), then initialize getChecked[] as a new boolean array
			 *  and set it's size to the total number of children associated with
			 *  the parent group
			*/
            boolean getChecked[] = new boolean[getChildrenCount (mGroupPosition)];

            // add getChecked[] to the mChildCheckStates hashmap using mGroupPosition as the key
            mChildCheckStates.put (mGroupPosition, getChecked);

            // set the check state of this position's checkbox based on the
            // boolean value of getChecked[position]
            childViewHolder.mCheckBox.setChecked (false);
        }

        childViewHolder.mCheckBox.setOnCheckedChangeListener (new OnCheckedChangeListener ()
        {

            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked)
            {

                if (isChecked) {

                    boolean getChecked[] = mChildCheckStates.get (mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put (mGroupPosition, getChecked);

                }
                else {

                    boolean getChecked[] = mChildCheckStates.get (mGroupPosition);
                    getChecked[mChildPosition] = isChecked;
                    mChildCheckStates.put (mGroupPosition, getChecked);
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable (int groupPosition, int childPosition)
    {
        return false;
    }

    @Override
    public boolean hasStableIds ()
    {
        return false;
    }

    public final class GroupViewHolder
    {
        TextView mGroupText;
    }

    public final class ChildViewHolder
    {

        TextView mChildStringText;
        CheckBox mCheckBox;
        TextView mChildNumberText;
    }
}


