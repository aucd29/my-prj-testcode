package org.teleal.cling.android.browser;

import java.io.File;
import java.util.ArrayList;

import org.teleal.cling.android.AndroidUpnpService;
import org.teleal.cling.controlpoint.ActionCallback;
import org.teleal.cling.model.action.ActionException;
import org.teleal.cling.model.action.ActionInvocation;
import org.teleal.cling.model.message.UpnpResponse;
import org.teleal.cling.model.meta.Service;
import org.teleal.cling.model.types.ErrorCode;
import org.teleal.cling.support.contentdirectory.callback.Browse;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.Item;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author Aquilegia
 */
public class ContentBrowserAdapter extends BaseAdapter implements
		OnItemClickListener, OnKeyListener {

	private Context mContext;
	private LayoutInflater mInflater;
	private AndroidUpnpService mUpnpService;
	private Service mService;
	private String mCurrentId;
	private Container rootContainer;
	private ContentNode rootNode;
	private ContentNode mCurrentRoot;
	private ArrayList<String> mList = new ArrayList<String>();
	private String mFilter = "3gp#amv#ape#asf#avi#flac#flv#hlv#mkv#mov#mp3#mp4#mpeg#mpg#rm#rmvb#tta#wav#wma#wmv";

	private static final String TAG = "UpnpBrowser";

	final boolean[] assertions = new boolean[3];

	public ContentBrowserAdapter(Context context, AndroidUpnpService upnpService, Service service) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mUpnpService = upnpService;
		mService = service;
		rootContainer = createRootContainer(service);
		rootNode = new ContentNode(null, rootContainer);
		setDirectory(rootNode);
	}
	
	public AndroidUpnpService getUpnpService() {
		return mUpnpService;
	}
	
	protected Container createRootContainer(Service service) {
		Container rootContainer = new Container();
		rootContainer.setId("0");
		rootContainer.setTitle("Content Directory on " + service.getDevice().getDisplayString());
		return rootContainer;
	}

	private void setDirectory(ContentNode containerNode) {
		mCurrentRoot = containerNode;
		mList.clear();

		if (mCurrentRoot.getChildNodes() == null) {
			ActionCallback actionCallback = new Browse(mService, containerNode.getContainer().getId(), BrowseFlag.DIRECT_CHILDREN) {
				public void received(ActionInvocation actionInvocation, DIDLContent didl) {
					//Log.d(TAG, "Received browse action DIDL descriptor, creating tree nodes");
					
					try {
						
						// Containers first
						for (Container childContainer : didl.getContainers()) {
							Log.d(TAG, "Received container : " + childContainer.getTitle());
							mCurrentRoot.addChildNode(new ContentNode(mCurrentRoot, childContainer));
						}
						
						// Now items
						for (Item childItem : didl.getItems()) {
							Log.d(TAG, "Received Item : " + childItem.getTitle());
							mCurrentRoot.addChildNode(new ContentNode(mCurrentRoot, childItem));
						}
					
					} catch (Exception ex) {
						//log.fine("Creating DIDL tree nodes failed: " + ex);
						actionInvocation.setFailure(
								new ActionException(ErrorCode.ACTION_FAILED, "Can't create tree child nodes: " + ex, ex)
						);
						failure(actionInvocation, null);
					}
					
					//assertEquals(didl.getContainers().size(), 3);
					//assertEquals(didl.getContainers().get(0).getTitle(), "My Music");
					//assertEquals(didl.getContainers().get(1).getTitle(), "My Photos");
					//assertEquals(didl.getContainers().get(2).getTitle(), "Album Art");
					
					assertions[0] = true;
				}
				
				@Override
				public void updateStatus(Status status) {
					if (!assertions[1] && status.equals(Status.LOADING)) {
						assertions[1] = true;
					} else if (assertions[1] && !assertions[2] && status.equals(Status.OK)) {
						assertions[2] = true;
					}
				}
				
				@Override
				public void failure(ActionInvocation invocation, UpnpResponse operation, String defaultMsg) {
					
				}
			};
			
			//mUpnpService.getControlPoint().execute(actionCallback);
			new ActionCallback.Default(actionCallback.getActionInvocation(), mUpnpService.getControlPoint()).run();
			actionCallback.success(actionCallback.getActionInvocation());
		}
		
		if (mCurrentRoot.getChildNodes() != null) {
			for (ContentNode childNode : mCurrentRoot.getChildNodes()) {
				if (childNode.isContainer())
					mList.add(childNode.getContainer().getTitle());
				else
					mList.add(childNode.getItem().getTitle());
			}
		} else
			mList.add("No Content");
		
		mCurrentId = mCurrentRoot.getContainer().getId();

		notifyDataSetChanged();
		
	}
	
	@Override
	public int getCount() {
		int count = mList.size();
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object obj = mList.get(position);
		return obj;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.list_item_icon_text, null);

			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.icon = (ImageView) convertView.findViewById(R.id.icon);

			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView
			// and the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}
		String name = mList.get(position);
		// Bind the data efficiently with the holder.
		holder.text.setText(name);
		if (mCurrentRoot.getChildNodes() != null) {
			ContentNode node = mCurrentRoot.getChildNodes().get(position);
			if (node.isContainer())
				holder.icon.setImageResource(R.drawable.bd_folder);
			else {
				String className = node.getItem().getClazz().getValue();
				if (className.contains("videoItem"))
					holder.icon.setImageResource(R.drawable.bd_movie);
				else if (className.contains("audioItem"))
					holder.icon.setImageResource(R.drawable.bd_music);
				else if (className.contains("imageItem"))
					holder.icon.setImageResource(R.drawable.bd_photo);
				else
					holder.icon.setImageResource(R.drawable.bd_unknown);
			}
		} else
			holder.icon.setImageResource(R.drawable.bd_unknown);

		return convertView;
	}

	static class ViewHolder {
		TextView text;
		ImageView icon;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		File file = new File(String.format("%s/%s", mCurrentId, mList.get(position)));
		if (position < mCurrentRoot.getChildContainers()) {
			setDirectory(mCurrentRoot.getChildNodes().get(position));
		}
		else if (mCurrentRoot.getChildNodes() != null) {
			ContentNode node = mCurrentRoot.getChildNodes().get(position);
			Log.d(TAG, "Item : " + node.getItem().getTitle());
			Log.d(TAG, "Item : " + node.getItem().getFirstResource().getValue());
			Log.d(TAG, "Item : " + node.getItem().getClazz().getValue());/* UPnP Class */

			Intent intent;
			ContentList contentList;
			String className = node.getItem().getClazz().getValue();

			if (className.contains("videoItem")) {
				intent = new Intent(mContext, PlayVideoActivity.class);
				contentList = new ContentList(ContentList.VIDEOITEM);
			} else if (className.contains("audioItem")) {
				intent = new Intent(mContext, PlayAudioActivity.class);
				contentList = new ContentList(ContentList.AUDIOITEM);
			} else if (className.contains("imageItem")) {
				intent = new Intent(mContext, PlayImageActivity.class);
				contentList = new ContentList(ContentList.IMAGEITEM);
			} else {
				Log.d(TAG, "The selected Item has unknown type.");
				return;
			}
			contentList.addContentList(mCurrentRoot.getChildNodes());
			
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("list", contentList.getTitleList());
			bundle.putStringArrayList("urilist", contentList.getUriList());
			bundle.putInt("index", contentList.getItemIndex(node)/*position - mCurrentRoot.getChildContainers()*/);
			/*
			bundle.putInt("index", 0);
			*/
			intent.putExtra("playlist", bundle);
			mContext.startActivity(intent);
		}
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!mCurrentId.equals("0")) {
				Log.d(TAG, "onKey: mCurrentId : " + mCurrentId);
				setDirectory(mCurrentRoot.getParentNode());
				return true;
			} else
				return false;
		}
		return false;
	}
}
