import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { TileComponent } from './tile/tile.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { AssetRisk } from './assetRisk/assetRisk.component';

@NgModule({
  imports: [
    CommonModule,
    RouterModule
  ],
  declarations: [TileComponent, HeaderComponent, FooterComponent, AssetRisk],
  exports: [TileComponent, HeaderComponent, FooterComponent, AssetRisk]
})
export class SharedModule { }
