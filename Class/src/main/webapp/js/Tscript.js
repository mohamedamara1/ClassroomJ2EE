document.getElementById("all").addEventListener("click",check1);
document.getElementById("none").addEventListener("click",check2);
var box=document.getElementsByName("matieres");
function check1 ()
{ 

    var box=document.getElementsByName("matieres");
    for (let i = 0; i < box.length; i++) 
    {
        box[i].checked=true;
    }
}
function check2 ()
{ 

    var box=document.getElementsByName("matieres");
    
    for (let i = 0; i < box.length; i++) {
        box[i].checked=false;
    }
}
function myValidation ()
{var test=false;
    for (let i = 0; i < box.length; i++) 
{
    if (box[i].checked==true)
    { test=true;break;}
}
if (!test){alert('Check at least one choice');returnToPreviousPage();
return false;}

}