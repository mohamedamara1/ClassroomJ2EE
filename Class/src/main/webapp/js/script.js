console.log("hello fro mstudent page");


document.getElementById("all").addEventListener("click",check1);
//var box=document.getElementsByClassName("classes");
var box=document.getElementsByName("matieres");

document.getElementById("test").addEventListener("click", (event) =>{
    console.log("clicked test button");
        console.log(event.target.value);
            console.log(event.target);


    console.log(event.target.id);
})
for (let i = 0; i < box.length; i++) 
{
    box[i].addEventListener("click",check2); 
}
function check1 ()
{ 

    var box=document.getElementsByName("matieres");
    if ((document.getElementById("all").checked)==true)
    {for (let i = 0; i < box.length; i++) 
    {
        box[i].checked=true;
    }}
    else
    { for (let i = 0; i < box.length; i++) {
        box[i].checked=false;
    }}
}
function check2 ()
{
    document.getElementById("all").checked=false;
}
function check3 ()
{var test=false;
    for (let i = 0; i < box.length; i++) 
{
    if (box[i].checked==true)
    { test=true;break;}
}
if (!test){alert('Check at least one choice');}

}

function myValidation (){
var test=false;
    for (let i = 0; i < box.length; i++) 
{
    if (box[i].checked==true)
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

