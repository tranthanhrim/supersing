package notactivity;


public class ItemNavigation {
	private String _label;
	private Integer _icon;
	
	public ItemNavigation(Integer icon, String label){
		set_icon(icon);
		set_label(label);
	}

	public String get_label() {
		return _label;
	}

	public void set_label(String _label) {
		this._label = _label;
	}

	public Integer get_icon() {
		return _icon;
	}

	public void set_icon(Integer _icon) {
		this._icon = _icon;
	}
	
	
}
