

var bounds = [
	[20, -150],[55, -40]
];

var map = L.map('map').setView([37.8, -96], 5);
map.zoomControl.setPosition('bottomright');
map.setMaxBounds(bounds);
map.on('drag', function() {
  map.panInsideBounds(bounds, { animate: false });
});

L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
	maxZoom: 18, minZoom: 5,
	attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
	'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
	'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
	id: 'mapbox.light'
}).addTo(map);

//-------------------------------------------------------------------------------------------
L.StateSelect = {};
L.StateSelect.states = statesData.features;
L.StateSelect = L.Control.extend({
	options: {
		position: 'topright',
		title: 'State',
		states: L.StateSelect.states,
	},
	onAdd: function(map) {
		this.div = L.DomUtil.create('div','leaflet-stateselect-container');
		this.select = L.DomUtil.create('select','leaflet-stateselect',this.div);
		var content = '';

		if (this.options.title.length > 0 ){
			content += '<option>'+this.options.title+'</option>';
		}

		var states = (this.options.states);


		for (i in states){
				content+='<option>'+Object.values(Object.values(Object.values(Object.values(states)[i])[2]))[0]+'</option>';
		}

		this.select.innerHTML = content;

		this.select.onmousedown = L.DomEvent.stopPropagation;

		return this.div;
	},
	on: function(type,handler){
		if (type == 'change'){
			this.onChange = handler;
			L.DomEvent.addListener(this.select,'change',this._onChange,this);
		} else if (type == 'click'){ //don't need this here probably, but for convenience?
			this.onClick = handler;
			L.DomEvent.addListener(this.select,'click',this.onClick,this);
		} else {
			console.log('StateSelect - cannot handle '+type+' events.')
		}
	},
	_onChange: function(e) {
		e.feature = this.options.states[this.select.selectedIndex-1];
		this.onChange(e);
	}
});

L.stateSelect = function(){
	return new L.StateSelect();
};

var select = L.stateSelect();
select.addTo(map);
var data = L.geoJson(statesData);
select.on('change', function(e){
  var state = L.geoJson(e.feature);
  map.fitBounds(state.getBounds());
});
// -----------------------------------------------------------------------------------------
//
// control that shows state info on hover
var info = L.control();
	info.onAdd = function (map) {
	this._div = L.DomUtil.create('div', 'info');
	this.update();
	return this._div;
};
info.update = function (props) {
  this._div.innerHTML = '<h4>US Population Density</h4>' +  (props ?
  	'<b>' + props.name + '</b><br />' + props.density + ' people / mi<sup>2</sup>'
  	: 'Hover over a state');
};
info.addTo(map);

	// get color depending on population density value
function getColor(d) {
	return d > 1000 ? '#800026' :
				d > 500  ? '#BD0026' :
				d > 200  ? '#E31A1C' :
				d > 100  ? '#FC4E2A' :
				d > 50   ? '#FD8D3C' :
				d > 20   ? '#FEB24C' :
				d > 10   ? '#FED976' :
							     '#FFEDA0';
}

function style(feature) {
	return {
	  weight: 2,
		opacity: 1,
		color: 'white',
		dashArray: '3',
		fillOpacity: 0.7,
		fillColor: getColor(feature.properties.density)
	};
}

function highlightFeature(e) {
	var layer = e.target;
	layer.setStyle({
	weight: 5,
	color: '#666',
	dashArray: '',
	fillOpacity: 0.7
  });

  if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
  	layer.bringToFront();
  }

  info.update(layer.feature.properties);
  }

var rijson;
var msjson;
var txjson;

var geojson;

function resetHighlight(e) {
	geojson.resetStyle(e.target);
	info.update();
}

function zoomToFeature(e) {

	map.fitBounds(e.target.getBounds());
}

function onEachFeature(feature, layer) {
	layer.on({
		mouseover: highlightFeature,
		mouseout: resetHighlight,
		click: zoomToFeature
	});
}

geojson = L.geoJson(statesData, {
			style: style,
			onEachFeature: onEachFeature
		});

rijson = L.geoJson(riData, {
	style: style,
	onEachFeature: onEachFeature
}).addTo(map);

msjson = L.geoJson(msData, {
	style: style,
	onEachFeature: onEachFeature
}).addTo(map);

txjson = L.geoJson(txData, {
	style: style,
	onEachFeature: onEachFeature
}).addTo(map);

rhodeIsland = L.geoJson(ri, {
  style: style,
  onEachFeature: onEachFeature
});

mississippi = L.geoJson(ms, {
  style: style,
  onEachFeature: onEachFeature
});

map.on('zoomend', function() {
var zoomlevel = map.getZoom();

if(zoomlevel > 9){
  map.removeLayer(rijson);
  map.removeLayer(msjson);
  map.addLayer(rhodeIsland);
  map.addLayer(mississippi);
  //add texas
}
if(zoomlevel >= 7 && zoomlevel < 9 ){
  if(map.hasLayer(msjson)){
    map.removeLayer(msjson);
    map.addLayer(mississippi);
  }
  if(map.hasLayer(rhodeIsland)){
    map.removeLayer(rhodeIsland);
    map.addLayer(rijson);
  }
  //add texas also
}
if(zoomlevel < 7){
  if(map.hasLayer(mississippi)){
    map.removeLayer(mississippi);
    map.addLayer(msjson);
  }
  if(map.hasLayer(rhodeIsland)){
    map.removeLayer(rhodeIsland);
    map.addLayer(rijson);
  }
}


});

L.Control.Sidebar = L.Control.extend(/** @lends L.Control.Sidebar.prototype */ {
    includes: (L.Evented.prototype || L.Mixin.Events),

    options: {
        position: 'left'
    },

    initialize: function (id, options) {
        var i, child;

        L.setOptions(this, options);

        // Find sidebar HTMLElement
        this._sidebar = L.DomUtil.get(id);

        // Attach .sidebar-left/right class
        L.DomUtil.addClass(this._sidebar, 'sidebar-' + this.options.position);

        // Attach touch styling if necessary
        if (L.Browser.touch)
            L.DomUtil.addClass(this._sidebar, 'leaflet-touch');

        // Find sidebar > div.sidebar-content
        for (i = this._sidebar.children.length - 1; i >= 0; i--) {
            child = this._sidebar.children[i];
            if (child.tagName == 'DIV' &&
                    L.DomUtil.hasClass(child, 'sidebar-content'))
                this._container = child;
        }

        // Find sidebar ul.sidebar-tabs > li, sidebar .sidebar-tabs > ul > li
        this._tabitems = this._sidebar.querySelectorAll('ul.sidebar-tabs > li, .sidebar-tabs > ul > li');
        for (i = this._tabitems.length - 1; i >= 0; i--) {
            this._tabitems[i]._sidebar = this;
        }

        // Find sidebar > div.sidebar-content > div.sidebar-pane
        this._panes = [];
        this._closeButtons = [];
        for (i = this._container.children.length - 1; i >= 0; i--) {
            child = this._container.children[i];
            if (child.tagName == 'DIV' &&
                L.DomUtil.hasClass(child, 'sidebar-pane')) {
                this._panes.push(child);

                var closeButtons = child.querySelectorAll('.sidebar-close');
                for (var j = 0, len = closeButtons.length; j < len; j++)
                    this._closeButtons.push(closeButtons[j]);
            }
        }
    },

    addTo: function (map) {
        var i, child;

        this._map = map;

        for (i = this._tabitems.length - 1; i >= 0; i--) {
            child = this._tabitems[i];
            var sub = child.querySelector('a');
            if (sub.hasAttribute('href') && sub.getAttribute('href').slice(0,1) == '#') {
                L.DomEvent
                    .on(sub, 'click', L.DomEvent.preventDefault )
                    .on(sub, 'click', this._onClick, child);
            }
        }

        for (i = this._closeButtons.length - 1; i >= 0; i--) {
            child = this._closeButtons[i];
            L.DomEvent.on(child, 'click', this._onCloseClick, this);
        }

        return this;
    },

     removeFrom: function(map) {
         console.log('removeFrom() has been deprecated, please use remove() instead as support for this function will be ending soon.');
         this.remove(map);
     },

    remove: function (map) {
        var i, child;

        this._map = null;

        for (i = this._tabitems.length - 1; i >= 0; i--) {
            child = this._tabitems[i];
            L.DomEvent.off(child.querySelector('a'), 'click', this._onClick);
        }

        for (i = this._closeButtons.length - 1; i >= 0; i--) {
            child = this._closeButtons[i];
            L.DomEvent.off(child, 'click', this._onCloseClick, this);
        }

        return this;
    },

    /**
     * Open sidebar (if necessary) and show the specified tab.
     *
     * @param {string} id - The id of the tab to show (without the # character)
     */
    open: function(id) {
        var i, child;

        // hide old active contents and show new content
        for (i = this._panes.length - 1; i >= 0; i--) {
            child = this._panes[i];
            if (child.id == id)
                L.DomUtil.addClass(child, 'active');
            else if (L.DomUtil.hasClass(child, 'active'))
                L.DomUtil.removeClass(child, 'active');
        }

        // remove old active highlights and set new highlight
        for (i = this._tabitems.length - 1; i >= 0; i--) {
            child = this._tabitems[i];
            if (child.querySelector('a').hash == '#' + id)
                L.DomUtil.addClass(child, 'active');
            else if (L.DomUtil.hasClass(child, 'active'))
                L.DomUtil.removeClass(child, 'active');
        }

        this.fire('content', { id: id });

        // open sidebar (if necessary)
        if (L.DomUtil.hasClass(this._sidebar, 'collapsed')) {
            this.fire('opening');
            L.DomUtil.removeClass(this._sidebar, 'collapsed');
        }

        return this;
    },

    /**
     * Close the sidebar (if necessary).
     */
    close: function() {
        // remove old active highlights
        for (var i = this._tabitems.length - 1; i >= 0; i--) {
            var child = this._tabitems[i];
            if (L.DomUtil.hasClass(child, 'active'))
                L.DomUtil.removeClass(child, 'active');
        }

        // close sidebar
        if (!L.DomUtil.hasClass(this._sidebar, 'collapsed')) {
            this.fire('closing');
            L.DomUtil.addClass(this._sidebar, 'collapsed');
        }

        return this;
    },

    /**
     * @private
     */
    _onClick: function() {
        if (L.DomUtil.hasClass(this, 'active'))
            this._sidebar.close();
        else if (!L.DomUtil.hasClass(this, 'disabled'))
            this._sidebar.open(this.querySelector('a').hash.slice(1));
    },

    /**
     * @private
     */
    _onCloseClick: function () {
        this.close();
    }
});


L.control.sidebar = function (id, options) {
    return new L.Control.Sidebar(id, options);
};



  	// map.attributionControl.addAttribution('Population data &copy; <a href="http://census.gov/">US Census Bureau</a>');
