import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TileComponent } from './tile/tile.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { AssetRisk } from './assetRisk/assetRisk.component';
import { AssetAnalysis } from './assetAnalysis/assetAnalysis.component';
import { LeafletMap } from './leafletMap/leafletMap.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [TileComponent, HeaderComponent, FooterComponent, AssetRisk, AssetAnalysis, LeafletMap],
  exports: [TileComponent, HeaderComponent, FooterComponent, AssetRisk, AssetAnalysis, LeafletMap]
})
export class SharedModule { }
