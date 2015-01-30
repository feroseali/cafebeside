package com.team.cafebeside;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends Activity implements OnItemClickListener {

	static final LauncherIcon[] ICONS = {
        new LauncherIcon(R.drawable.icon_one, "Todays Menu", "icon_one.png"),
        new LauncherIcon(R.drawable.icon_two, "My Orders", "icon_two.png"),
        new LauncherIcon(R.drawable.icon_one, "My Bills", "icon_one.png"),
        new LauncherIcon(R.drawable.icon_two, "Contact", "icon_two.png"),
};
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toast.makeText(getApplicationContext(), "Welcome To Cafe Beside.\nPlease place your order.", Toast.LENGTH_LONG).show();
        GridView gridview = (GridView) findViewById(R.id.dashboard_grid);
        gridview.setAdapter(new ImageAdapter(this));
        gridview.setOnItemClickListener(this);
        gridview.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});
    }

    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        Intent intent = new Intent(this, TodaysMenu.class);
       // intent.putExtra(EXTRA_MAP, ICONS[position].map);
        startActivity(intent);
    }
    
    static class LauncherIcon {
        final String text;
        final int imgId;
        
 
        public LauncherIcon(int imgId, String text, String map) {
            super();
            this.imgId = imgId;
            this.text = text;
         
        }
 
    }
    
    
    static class ImageAdapter extends BaseAdapter {
        private Context mContext;
 
        public ImageAdapter(Context c) {
            mContext = c;
        }
        @Override
        public int getCount() {
            return ICONS.length;
        }

        @Override
        public LauncherIcon getItem(int position) {
            return null;
        }
 
        public long getItemId(int position) {
            return 0;
        }
 
        static class ViewHolder {
            public ImageView icon;
            public TextView text;
        }
 
        // Create a new ImageView for each item referenced by the Adapter
        @SuppressLint("InflateParams") @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
 
                v = vi.inflate(R.layout.dashboard_icon, null);
                holder = new ViewHolder();
                holder.text = (TextView) v.findViewById(R.id.dashboard_icon_text);
                holder.icon = (ImageView) v.findViewById(R.id.dashboard_icon_img);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }
            	
            holder.icon.setImageResource(ICONS[position].imgId);
            holder.text.setText(ICONS[position].text);
 
            return v;
        }

    }
    
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
