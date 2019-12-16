var precinctKeys;
var currState;
var algoBegin = true;
var phase0ran = 0;
var phase1ran = 0;
var stateElection = null;
var p2res;
var p2disttable;
//----------------Setting----------------
var electOpts = $("input[name='ElectionData']");
var electionSetting = $("input[name='ElectionData']:checked").val();
var electionYear = electionSetting.split(" ")[0];
var electionType = electionSetting.split(" ")[1];

//----------------Phase0----------------
var phase0btn = $('#runPhase0');
var votingThresh = 50;
var blocThresh = 50;
var totWhite, totBlack, totHispanic, totPacific, totAsian, totNative, totOther, totPopulation;
var phase0Data;
//----------------Phase1-2----------------
var selectedPhase = $("input[name='PhaseType']:checked").val();
var runAlgoBtn = $('#runAlgo');
var iterateMe = $("#stepThrough");

var demoOpts = $("input[name='demoType']");
var selectedDemo = $("input[name='demoType']:checked").val();   //this only checks ONE need to get the other if avail

var targetDis = $("#targetMM");
var targetDisVal;

var phase1Data;

var minoritySelected = '';
var minorityOpt = $("input[name='minority-cb']")
//----------------------Slider Stuff-------------------
var fairnessMin = 0;
var fairnessMax = 0;
var reokCompactMin = 0;
var reokCompactMax = 0;
var convexCompactMin = 0;
var convexCompactMax = 0;
var edgeCompactMin = 0;
var edgeCompactMax = 0;
var popEqualMin = 0;
var popEqualMax = 0;
var popHomoMin = 0;
var popHomoMax = 0;
var efficiencyMin = 0;
var efficiencyMax = 0;
var competitiveMin = 0;
var competitiveMax = 0;
var republicGerryMin = 0;
var republicGerryMax = 0;
var democratGerryMin = 0;
var democratGerryMax = 0;
var countyCompactMin = 0;
var countyCompactMax = 0;

//-----------------------------------------------------
var distLayer = L.layerGroup();
var precLayer = L.layerGroup();
var distPrecinct = {}
var districtx = {}
var districtxData = {}
var electionDic = {}
electionDic['congressional2016'] = {}
electionDic['congressional2018'] = {}
//-----------------------------------------------------
var geojson;
var texas;
var texasDistrictsLayer;
var bounds = [[20, -150],[55, -40]];
var usCenter = [37.8, -96];
var map;
//-----------------------------------------------------
var body = $('body');
var nav = $('nav');
var outerMenuBtn = $('#outer-menu-btn');
var innerMenuBtn = $('#inner-menu-btn');
var mapContent = $('#selectMapContent');
//-----------------------------------------------------
var disColor = ['#3078dd','#47c871','#72b189','#0de85a','#6d73c4','#57ab0f','#3078dd','#5186af','#3fa20b','#a490a0',
                '#71aabe','#1940fe','#5bc4a3','#9a4d46','#866dc6','#80f4d0','#b0c7d7','#137a9d','#4d6d3a','#388f7a',
                '#bfb119','#1e5c1e','#3d9ed7','#518d92','#420f26','#b81163','#973a02','#413db1','#00718e','#08e548',
                '#191760','#c82d7d','#cdde56','#88670d','#612d87','#371b95','#eb95e9']
var selectedMinority = 0;
var maxSelCheckbox = 2;
var stateInit = 0;
//-----------------------------------------------------
var texas_precincts;  // prob dont need
var zoomin;

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}
