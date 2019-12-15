function loadjscssfile(filename, filetype, fn){

    var fileref=document.createElement('script')
      fileref.setAttribute("type","text/javascript")
      fileref.setAttribute("src", filename)
      fileref.async=true
      fileref.onload = fn;
     if (typeof fileref!="undefined")
      document.getElementsByTagName("head")[0].appendChild(fileref)
}
