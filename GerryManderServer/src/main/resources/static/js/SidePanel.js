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
}

function sliderChange(id){
  console.log('Slider button was selected');
  console.log(id);
}

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

function sliderToggle(){
  var quoteButton = $('.sliders-button'),
    blockquote = $('.sliders-block');
  blockquote.slideToggle(1000);
}
