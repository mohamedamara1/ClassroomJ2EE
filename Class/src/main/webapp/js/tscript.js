
console.log("should now work");

//  CHECK ALL ,  UNCHECK ALL
var buttons = document.getElementsByTagName("button");
for (let button of buttons ){
    if  ( button.innerHTML == "Check all"){
        button.addEventListener("click", (event) =>{
            console.log("pressed check all button");
            Array.from(document.getElementsByName(event.target.name))
            .filter(balise => balise.tagName != "BUTTON")
            .forEach(input => input.checked = true)
        })
    }
    else if (button.innerHTML == "Uncheck all"){
        button.addEventListener("click", (event) =>{
            console.log("pressed uncheck all button");
            Array.from(document.getElementsByName(event.target.name))
            .filter(balise => balise.tagName != "BUTTON")
            .forEach(input => input.checked = false)
        })
    }
}



// CHECK ATLEAST ONE ITEM

var checkboxes = Array.from(document.getElementsByTagName("input"));

function myValidation (){
var test=false;
    for (let i = 0; i < checkboxes.length; i++) 
{
    if (checkboxes[i].checked==true)
    { test=true;break;}
}
if (!test){
alert('Check at least one choice');
return false;
}
else{
console.log("returned true");
return true;
}

}

//
