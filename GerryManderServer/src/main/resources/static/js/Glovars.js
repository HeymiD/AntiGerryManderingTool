var selectedMinority = 0;
var maxSelCheckbox = 2;
var algorithms = ['Select Algorithm', 'Algorithm 1', 'Algorithm 2', 'Algorithm 3'];

var phaseSelector = $('#phaseType');
var algorithmSelector = $('#algorithmType');
var runAlgoBtn = $('#runAlgo');
var phase0btn = $('#runPhase0');

var body = $('body');
var nav = $('nav');
var outerMenuBtn = $('#outer-menu-btn');
var innerMenuBtn = $('#inner-menu-btn');
var mapContent = $('#selectMapContent');

var electionSetting = $("input[name='ElectionData']:checked").val();
var selectedDemo = $("input[name='demoType']:checked").val();
var demoOpts = $("input[name='demoType']");
var electOpts = $("input[name='ElectionData']");
var electionYear = electionSetting.split(" ")[0];
var electionType = electionSetting.split(" ")[1];

var votingThresh = 50;
var blocThresh = 50;

// console.log(votingTresh.val());
var geojson;
var texas;
var texasDistrictsLayer;
var bounds = [[20, -150],[55, -40]];


var usCenter = [37.8, -96];
var precinctKeys;
var phase0Data;
var stateInit = 0;
var currState;
var texas_precincts;
//var precinctDict = {}
var distPrecinct = {}
var districtx = {}
var districtxData = {}
var electionDic = {}
electionDic['congressional2016'] = {}
electionDic['congressional2018'] = {}
var distLayer = L.layerGroup();
var precLayer = L.layerGroup();
var disColor = ['#3078dd','#47c871','#72b189','#0de85a','#6d73c4','#57ab0f','#3078dd','#5186af','#3fa20b','#a490a0',
                '#71aabe','#1940fe','#5bc4a3','#9a4d46','#866dc6','#80f4d0','#b0c7d7','#137a9d','#4d6d3a','#388f7a',
                '#bfb119','#1e5c1e','#3d9ed7','#518d92','#420f26','#b81163','#973a02','#413db1','#00718e','#08e548',
                '#191760','#c82d7d','#cdde56','#88670d','#612d87','#371b95','#eb95e9']
