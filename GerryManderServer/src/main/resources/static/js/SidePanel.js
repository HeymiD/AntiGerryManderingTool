
/* Effects of Tabs */
$(".tabs .tab-links a").on('click', function(e) {
  var selectedTab = $(this).attr('href');
  $(selectedTab).show().siblings().hide();
  $(this).parent('li').addClass('active').siblings().removeClass('active');
});

/* Allow only MAX 2 Minorities to be selected */
$(".minority-cb").on('change', function(e){
  selectedMinority = $(".minority-cb").siblings(':checked').length;
  if(selectedMinority > maxSelCheckbox){
    this.checked = false;
  }
});

/* Switches between Hamburger Button */
function hambrgrToggle(id) {
  // console.log(id);
  let menuBtnBlock;
  if(id == 'outer-menu-btn'){
    outerMenuBtn.hide();
  }
  else{
    outerMenuBtn.show();
  }
  body.toggleClass('active-nav');
  // $('#prereq-block').css('display', 'None')
  // $('#abutton-block').css('display', 'None')
  // $('#objectives-block').css('display', 'None')

}

$(function(){
  $(".phase0-slider").slider({
    range: false,
    orientation: "horizontal",
    min: 50,
    max: 100,
    values:[50],
    step: 1,
    slide:
      function(event,ui){
        if(this.id == 'phase0slider1'){
          $("#votingThreshold").val(ui.values[0]);
          votingThresh = ui.values[0];
        }
        else if(this.id == 'phase0slider2'){
          $("#blocThreshold").val(ui.values[0]);
          blocThresh = ui.values[0];
        }
      }
  });
  $("#votingThreshold").val($(".phase0-slider").slider("values", 0));
  $("#blocThreshold").val($(".phase0-slider").slider("values", 0));
});



/* Initializing Sliders */
$(function () {
  $(".slider-range").slider({
  range: true,
  orientation: "horizontal",
  min: 0,
  max: 100,
  values: [0, 100],
  step: 1,
  slide:
    function (event, ui) {
      if (ui.values[0] == ui.values[1]) {
          return false;
      }
      if(this.id == 'population-slider'){
        $("#min-population").val(ui.values[0]);
        $("#max-population").val(ui.values[1]);
      }
      else if(this.id == 'compactness-slider'){
        $("#min-compactness").val(ui.values[0]);
        $("#max-compactness").val(ui.values[1]);
      }
      else if(this.id == 'fairness-slider'){
        $("#min-fairness").val(ui.values[0]);
        $("#max-fairness").val(ui.values[1]);
      }
      else if(this.id == 'equality-slider'){
        $("#min-equality").val(ui.values[0]);
        $("#max-equality").val(ui.values[1]);
      }
      else if(this.id == 'minority-population-slider'){
        $("#min-minority-population").val(ui.values[0]);
        $("#max-minority-population").val(ui.values[1]);
      }
    }
  });
  $("#min-population").val($(".slider-range").slider("values", 0));
  $("#max-population").val($(".slider-range").slider("values", 1));
  $("#min-compactness").val($(".slider-range").slider("values", 0));
  $("#max-compactness").val($(".slider-range").slider("values", 1));
  $("#min-fairness").val($(".slider-range").slider("values", 0));
  $("#max-fairness").val($(".slider-range").slider("values", 1));
  $("#min-equality").val($(".slider-range").slider("values", 0));
  $("#max-equality").val($(".slider-range").slider("values", 1));
  $("#min-minority-population").val($(".slider-range").slider("values", 0));
  $("#max-minority-population").val($(".slider-range").slider("values", 1));

});

function text2slider2(id){
    var currSlider;
    var currVal,val;
    console.log(id);
    switch(id){
      case 'votingThreshold':
        currSlider = $('#phase0slider1');
        val = $('#votingThreshold');
        votingThresh = id;
        break;
      case 'blocThreshold':
        currSlider = $('#phase0slider2');
        blocThresh = id;
        val = $('#blocThreshold');
        break;
    }
    currVal = parseInt(val.val());

    if(currVal <= 50 || currVal > 100){
      currVal = 50;
      val.val(currVal);
    }
    console.log(currSlider);
    currSlider.slider({
      values: [currVal]
    });


}
/* Setting slider values */
function text2slider(id){
  // console.log('inside sliderChanger');
  var currSlider = id.substring(id.indexOf('-')+1,id.length);
  var currMinVal, currMaxVal, min, max;
  switch(currSlider){
    case 'population':
      currSlider = $("#population-slider");
      min = $("#min-population");
      max = $("#max-population");
      break;
    case 'compactness':
      currSlider = $("#compactness-slider");
      min = $("#min-compactness");
      max = $("#max-compactness");
      break;
    case 'fairness':
      currSlider = $("#fairness-slider");
      min = $("#min-fairness");
      max = $("#max-fairness");
      break;
    case 'equality':
      currSlider = $("#equality-slider");
      min = $("#min-equality");
      max = $("#max-equality");
      break;
    case 'minority-population':
      currSlider = $("#minority-population-slider");
      min = $("#min-minority-population");
      max = $("#max-minority-population");
      break;
  }
  currMinVal = parseInt(min.val());
  currMaxVal = parseInt(max.val());
  if(currMinVal == currMaxVal){
    currMaxVal = 100;
    min.val(currMinVal);
    max.val(currMaxVal);
  }
  currSlider.slider({
    values: [currMinVal, currMaxVal]
  });
}

$("input[type='radio']").click(function(){
  electionSetting = $("input[name='ElectionData']:checked").val();
  electionYear = electionSetting.split(" ")[0];
  elecType = electionSetting.split(" ")[1];
  // console.log(electionType + electionSetting + electionYear);
});

function updatePhase1(){
    console.log("Phase 1 Results: ")
//    getPhaseZeroData(electionType.val()+electionYear.val(),0.75,0.8);
    getPhaseOneData(electionSetting,0.75,0.6,false,36,"ASIAN,PACISLAND");
    console.log(phase0Data)
}
