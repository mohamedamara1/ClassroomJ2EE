document.getElementById("all").addEventListener("click",check1);
//var box=document.getElementsByClassName("classes");
var box=document.getElementsByName("matieres")

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


