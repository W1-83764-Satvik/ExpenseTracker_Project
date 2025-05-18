import React from "react";
import "../page/HomePgContent.css";
import icon1 from "../images/icon1.png"
import icon2 from "../images/icon2.png"
import icon3 from "../images/icon3.png"
import doctor1 from "../images/doctor1.jpeg"
import medicalcheckup from "../images/medical-checkup.png"
import gyncare from "../images/gyn-care.png"
import pharmacy from "../images/pharmacy.png"
import neurology from "../images/neurology.png"
import nursingservice from "../images/nursingservice.png"
import sleepcenter from "../images/sleepcenter.png"
const HomePgContent = () => {
  return (
    <>
    <div
      className="container ct1 animate__animated animate__fadeInLeft"
      
    >
      <div className="row">
        <div className="col-6">
          <h4>MedEase-Virtual Reception</h4>
          <p><i>~ Provide a bridge between patients and doctors</i></p>
          <div class="box">
            <ol>
              <li>
                <strong> Allows clients to book at any time and place</strong>
                <br />
                <br />
              </li>
              <li>
                <strong>Book Appointments and Check Past Appointments</strong>
                <br />
                <br />
              </li>
              <li>
                <strong>Only use friendly environment</strong>
                <br />
                <br />
              </li>
            </ol>
          </div>
        </div>
      </div>
    </div>
    <div class=" container3 animate__animated animate__fadeInUp">
	<div class="boxA">
		<img src={icon1}/>
		<h3>Make an appointment</h3>
		<p>
		Patients can talk to you from the comfort of their own home
		</p>
	</div>
	
	<div class="boxB">
		<img src={icon2}/>
		<h3>Help by specialist</h3>
		<p>While some physicians specialize in diagnosing and treating the whole body
		</p>
	</div>
	
	<div class="boxC">
		<img src={icon3}/>
		<h3>Get diagnostic report</h3>
		<p >
		A medical report is an report of work on a medical examination of patient
		</p>
	</div>
</div>

    </>
  );
};

export default HomePgContent;
