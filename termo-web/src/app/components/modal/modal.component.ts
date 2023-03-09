import { Component, OnInit, Input } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { UserGameInfo } from 'src/app/models/users';
import { Chart, registerables } from 'chart.js';
import  ChartDataLabels  from 'chartjs-plugin-datalabels';
Chart.register(...registerables);
Chart.register(ChartDataLabels);


@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.scss']
})
export class ModalComponent implements OnInit {

  @Input() userInfo!: UserGameInfo;
  @Input() success!: boolean;
  @Input() currentRow!: number;

  constructor(public activeModal: NgbActiveModal) { }

  xValues : number[] = [];
  yValues : number[] = [];

  ngOnInit(): void {

    this.userInfo.histogramList.forEach( histogram => {
      this.xValues.push(histogram.attempts);
      this.yValues.push(histogram.sum);
    })

    new Chart("myChart", {
      type: "bar",      
      data: {
          labels: this.xValues,          
          datasets: [{
          backgroundColor: "#2a9d8f",
          label: 'Número de jogos por tentativas',          
          data: this.yValues
          }]
      },  

      options: {
        indexAxis: 'y',
        scales: {
          y: { title: {display: true, align: 'center', text: "Tentativas", color: "white"} },
          x: { title: {display: true, align: 'center', text: "Jogos", color: "white"} }
        },        
        plugins: {
          datalabels: {
            color: 'white',    
            align: 'start',  
            anchor: 'end',      
            font: {
              weight: 'bold'
            },
            formatter: Math.round
          }
        } 
      }
    });
  }

  startNewGame() {
    this.activeModal.close("newgame");
  }

  checkNewGame() {
    if(!this.success && this.currentRow < 6) {
      return true;
    } else {
      return false;
    }
  }





}
