import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { MatTableModule, MatSort, MatTableDataSource, MatDatepickerModule, MatNativeDateModule } from '@angular/material';
import { DashboardChartComponent } from './dashboard-chart.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';


describe('DashboardTableComponent', () => {
  let component: DashboardChartComponent;
  let fixture: ComponentFixture<DashboardChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [
        MatTableModule,
        FormsModule,
        ReactiveFormsModule,
        MatFormFieldModule,
        MatSelectModule,
        MatNativeDateModule,
        MatDatepickerModule
      ],
      declarations: [ DashboardChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });


});
