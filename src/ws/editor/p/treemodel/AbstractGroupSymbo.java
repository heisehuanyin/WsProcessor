package ws.editor.p.treemodel;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGroupSymbo extends AbstractNodeSymbo implements FeatureGroupSymbo{
	private ArrayList<FeatureNodeSymbo> con = new ArrayList<>();
	
	
	public AbstractGroupSymbo(TreeModel m) {
		super(m);
	}

	@Override
	public int kind() {
		return FeatureNodeSymbo.KindGroup;
	}

	@Override
	public int getChildCount() {
		return this.con.size();
	}

	@Override
	public FeatureNodeSymbo getChildAtIndex(int index) {
		return this.con.get(index);
	}
	
	@Override
	public int getChildIndex(FeatureNodeSymbo child) {
		return this.con.indexOf(child);
	}
	
	//=================================================

	@Override
	public void removeChild(FeatureNodeSymbo one) {
		this.con.remove(one);
		NodeGeneralEvent e = new NodeGeneralEvent(this,"移除子节点："+one.getClass().getName());
		this.pushRemoveEvent(e);
		this.removeChild_External(one);
		
		one.initParent(null);
	}
	protected abstract void removeChild_External(FeatureNodeSymbo one);

	private void pushRemoveEvent(NodeGeneralEvent e) {
		if(e.getSource() == this) {
			List<NodeEventListener> ls = this.getNodeEventListenerList();
			for(NodeEventListener l:ls) {
				l.nodeRemove(e);
			}
		}else {
			e.recordTreePath(this);
		}
		
		FeatureGroupSymbo x = this.getParent();
		if(x==null)
			return;
		((AbstractGroupSymbo)x).pushRemoveEvent(e);
	}

	@Override
	public void insertChildAtIndex(FeatureNodeSymbo node, int index) {
		this.con.add(index, node);
		NodeGeneralEvent e = new NodeGeneralEvent(this, "插入子节点："+ node.getClass().getName());
		this.pushInsertEvent(e);
		this.insertChildAtIndex_External(node, index);
		
		node.initParent(this);
	}
	protected abstract void insertChildAtIndex_External(FeatureNodeSymbo node, int index);

	
	private void pushInsertEvent(NodeGeneralEvent e) {
		if(e.getSource() == this) {
			List<NodeEventListener> ls= this.getNodeEventListenerList();
			for(NodeEventListener l:ls) {
				l.nodeInsert(e);
			}
		}else {
			e.recordTreePath(this);
		}
		
		FeatureGroupSymbo x = this.getParent();
		if(x == null)
			return;
		
		((AbstractGroupSymbo)x).pushInsertEvent(e);
	}
	
}
