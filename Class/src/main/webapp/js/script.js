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

$("#submit").click(function(e){

    var number_of_checked_checkbox= $("input[name=matieres]:checked").length;
    if(number_of_checked_checkbox==0){
        alert("select any one");
    }else{
        $("#selection").submit();
    }

         });

